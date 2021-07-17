package ${package};

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
public class RefreshConfiguration {

    @Bean
    public RefreshScope refreshScope() {
        return new RefreshScope();
    }

    public static class RefreshScope implements ApplicationContextAware, Scope, BeanFactoryPostProcessor,
            BeanDefinitionRegistryPostProcessor, ApplicationListener<ContextRefreshedEvent>, DisposableBean {

        private Logger logger = LoggerFactory.getLogger(RefreshScope.class);

        private String name = "refresh";

        private boolean eager = true;

        private ApplicationContext context;

        private ConfigurableListableBeanFactory beanFactory;

        private BeanLifecycleWrapperCache cache = new BeanLifecycleWrapperCache(new StandardScopeCache());

        private String id;

        private StandardEvaluationContext evaluationContext;

        private Map<String, Exception> errors = new ConcurrentHashMap<>();

        private ConcurrentMap<String, ReadWriteLock> locks = new ConcurrentHashMap<>();

        private BeanDefinitionRegistry registry;

        @Override
        public void setApplicationContext(ApplicationContext context) throws BeansException {
            this.context = context;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            this.beanFactory = configurableListableBeanFactory;
            beanFactory.registerScope(name, this);
            setSerializationId(beanFactory);
        }

        static RuntimeException wrapIfNecessary(Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                return (RuntimeException) throwable;
            }
            if (throwable instanceof Error) {
                throw (Error) throwable;
            }
            return new IllegalStateException(throwable);
        }

        @Override
        public void destroy() {
            List<Throwable> errors = new ArrayList<Throwable>();
            Collection<BeanLifecycleWrapper> wrappers = this.cache.clear();
            for (BeanLifecycleWrapper wrapper : wrappers) {
                try {
                    Lock lock = this.locks.get(wrapper.getName()).writeLock();
                    lock.lock();
                    try {
                        wrapper.destroy();
                    }
                    finally {
                        lock.unlock();
                    }
                }
                catch (RuntimeException e) {
                    errors.add(e);
                }
            }
            if (!errors.isEmpty()) {
                throw wrapIfNecessary(errors.get(0));
            }
            this.errors.clear();
        }

        protected boolean destroy(String name) {
            BeanLifecycleWrapper wrapper = this.cache.remove(name);
            if (wrapper != null) {
                Lock lock = this.locks.get(wrapper.getName()).writeLock();
                lock.lock();
                try {
                    wrapper.destroy();
                }
                finally {
                    lock.unlock();
                }
                this.errors.remove(name);
                return true;
            }
            return false;
        }

        @Override
        public Object get(String name, ObjectFactory<?> objectFactory) {
            BeanLifecycleWrapper value = this.cache.put(name, new BeanLifecycleWrapper(name, objectFactory));
            this.locks.putIfAbsent(name, new ReentrantReadWriteLock());
            try {
                return value.getBean();
            }
            catch (RuntimeException e) {
                this.errors.put(name, e);
                throw e;
            }
        }

        @Override
        public Object remove(String name) {
            BeanLifecycleWrapper value = this.cache.remove(name);
            if (value == null) {
                return null;
            }
            // Someone might have added another object with the same key, but we
            // keep the method contract by removing the
            // value we found anyway
            return value.getBean();
        }

        @Override
        public void registerDestructionCallback(String name, Runnable callback) {
            BeanLifecycleWrapper value = this.cache.get(name);
            if (value == null) {
                return;
            }
            value.setDestroyCallback(callback);
        }

        @Override
        public Object resolveContextualObject(String key) {
            Expression expression = parseExpression(key);
            return expression.getValue(this.evaluationContext, this.beanFactory);
        }

        private Expression parseExpression(String input) {
            if (input != null && input.trim().length() > 0) {
                ExpressionParser parser = new SpelExpressionParser();
                try {
                    return parser.parseExpression(input);
                }
                catch (ParseException e) {
                    throw new IllegalArgumentException("Cannot parse expression: " + input, e);
                }

            }
            else {
                return null;
            }
        }

        @Override
        public String getConversationId() {
            return name;
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
            for (String name : registry.getBeanDefinitionNames()) {
                BeanDefinition definition = registry.getBeanDefinition(name);
                if (definition instanceof RootBeanDefinition) {
                    RootBeanDefinition root = (RootBeanDefinition) definition;
                    if (root.getDecoratedDefinition() != null && root.hasBeanClass()
                            && root.getBeanClass() == ScopedProxyFactoryBean.class) {
                        if (name.equals(root.getDecoratedDefinition().getBeanDefinition().getScope())) {
                            root.setBeanClass(LockedScopedProxyFactoryBean.class);
                            root.getConstructorArgumentValues().addGenericArgumentValue(this);
                            // surprising that a scoped proxy bean definition is not already
                            // marked as synthetic?
                            root.setSynthetic(true);
                        }
                    }
                }
            }
        }

        private void setSerializationId(ConfigurableListableBeanFactory beanFactory) {

            if (beanFactory instanceof DefaultListableBeanFactory) {

                String id = this.id;
                if (id == null) {
                    List<String> list = new ArrayList<>(Arrays.asList(beanFactory.getBeanDefinitionNames()));
                    Collections.sort(list);
                    String names = list.toString();
                    logger.debug("Generating bean factory id from names: " + names);
                    id = UUID.nameUUIDFromBytes(names.getBytes()).toString();
                }

                logger.info("BeanFactory id=" + id);
                ((DefaultListableBeanFactory) beanFactory).setSerializationId(id);

            }
            else {
                logger.warn(
                        "BeanFactory was not a DefaultListableBeanFactory, scoped proxy beans " + "cannot be serialized.");
            }

        }

        protected ReadWriteLock getLock(String beanName) {
            return this.locks.get(beanName);
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            if (event.getApplicationContext() == this.context && this.eager && this.registry != null) {
                for (String name : this.context.getBeanDefinitionNames()) {
                    BeanDefinition definition = this.registry.getBeanDefinition(name);
                    if (name.equals(definition.getScope()) && !definition.isLazyInit()) {
                        Object bean = this.context.getBean(name);
                        if (bean != null) {
                            bean.getClass();
                        }
                    }
                }
            }
        }
    }

    interface ScopeCache {

        /**
         * Removes the object with this name from the cache.
         * @param name The object name.
         * @return The object removed, or null if there was none.
         */
        Object remove(String name);

        /**
         * Clears the cache and returns all objects in an unmodifiable collection.
         * @return All objects stored in the cache.
         */
        Collection<Object> clear();

        /**
         * Gets the named object from the cache.
         * @param name The name of the object.
         * @return The object with that name, or null if there is none.
         */
        Object get(String name);

        /**
         * Put a value in the cache if the key is not already used. If one is already present
         * with the name provided, it is not replaced, but is returned to the caller.
         * @param name The key.
         * @param value The new candidate value.
         * @return The value that is in the cache at the end of the operation.
         */
        Object put(String name, Object value);

    }

    public static class StandardScopeCache implements ScopeCache {

        private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

        public Object remove(String name) {
            return this.cache.remove(name);
        }

        public Collection<Object> clear() {
            Collection<Object> values = new ArrayList<Object>(this.cache.values());
            this.cache.clear();
            return values;
        }

        public Object get(String name) {
            return this.cache.get(name);
        }

        public Object put(String name, Object value) {
            Object result = this.cache.putIfAbsent(name, value);
            if (result != null) {
                return result;
            }
            return value;
        }

    }

    private static class BeanLifecycleWrapperCache {

        private final ScopeCache cache;

        BeanLifecycleWrapperCache(ScopeCache cache) {
            this.cache = cache;
        }

        public BeanLifecycleWrapper remove(String name) {
            return (BeanLifecycleWrapper) this.cache.remove(name);
        }

        public Collection<BeanLifecycleWrapper> clear() {
            Collection<Object> values = this.cache.clear();
            Collection<BeanLifecycleWrapper> wrappers = new LinkedHashSet<BeanLifecycleWrapper>();
            for (Object object : values) {
                wrappers.add((BeanLifecycleWrapper) object);
            }
            return wrappers;
        }

        public BeanLifecycleWrapper get(String name) {
            return (BeanLifecycleWrapper) this.cache.get(name);
        }

        public BeanLifecycleWrapper put(String name, BeanLifecycleWrapper value) {
            return (BeanLifecycleWrapper) this.cache.put(name, value);
        }

    }

    private static class BeanLifecycleWrapper {

        private final String name;

        private final ObjectFactory<?> objectFactory;

        private volatile Object bean;

        private Runnable callback;

        BeanLifecycleWrapper(String name, ObjectFactory<?> objectFactory) {
            this.name = name;
            this.objectFactory = objectFactory;
        }

        public String getName() {
            return this.name;
        }

        public void setDestroyCallback(Runnable callback) {
            this.callback = callback;
        }

        public Object getBean() {
            if (this.bean == null) {
                synchronized (this.name) {
                    if (this.bean == null) {
                        this.bean = this.objectFactory.getObject();
                    }
                }
            }
            return this.bean;
        }

        public void destroy() {
            if (this.callback == null) {
                return;
            }
            synchronized (this.name) {
                Runnable callback = this.callback;
                if (callback != null) {
                    callback.run();
                }
                this.callback = null;
                this.bean = null;
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            BeanLifecycleWrapper other = (BeanLifecycleWrapper) obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

    }

    /**
     * A factory bean with a locked scope.
     *
     * @param <S> - a generic scope extension
     */
    @SuppressWarnings("serial")
    public static class LockedScopedProxyFactoryBean<S extends RefreshConfiguration.RefreshScope> extends ScopedProxyFactoryBean
            implements MethodInterceptor {

        private Logger logger = LoggerFactory.getLogger(LockedScopedProxyFactoryBean.class);

        private final S scope;

        private String targetBeanName;

        public LockedScopedProxyFactoryBean(S scope) {
            this.scope = scope;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            super.setBeanFactory(beanFactory);
            Object proxy = getObject();
            if (proxy instanceof Advised) {
                Advised advised = (Advised) proxy;
                advised.addAdvice(0, this);
            }
        }

        @Override
        public void setTargetBeanName(String targetBeanName) {
            super.setTargetBeanName(targetBeanName);
            this.targetBeanName = targetBeanName;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            Method method = invocation.getMethod();
            if (AopUtils.isEqualsMethod(method) || AopUtils.isToStringMethod(method)
                    || AopUtils.isHashCodeMethod(method) || isScopedObjectGetTargetObject(method)) {
                return invocation.proceed();
            }
            Object proxy = getObject();
            ReadWriteLock readWriteLock = this.scope.getLock(this.targetBeanName);
            if (readWriteLock == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("For bean with name [" + this.targetBeanName
                            + "] there is no read write lock. Will create a new one to avoid NPE");
                }
                readWriteLock = new ReentrantReadWriteLock();
            }
            Lock lock = readWriteLock.readLock();
            lock.lock();
            try {
                if (proxy instanceof Advised) {
                    Advised advised = (Advised) proxy;
                    ReflectionUtils.makeAccessible(method);
                    return ReflectionUtils.invokeMethod(method, advised.getTargetSource().getTarget(),
                            invocation.getArguments());
                }
                return invocation.proceed();
            }
            // see gh-349. Throw the original exception rather than the
            // UndeclaredThrowableException
            catch (UndeclaredThrowableException e) {
                throw e.getUndeclaredThrowable();
            }
            finally {
                lock.unlock();
            }
        }

        private boolean isScopedObjectGetTargetObject(Method method) {
            return method.getDeclaringClass().equals(ScopedObject.class) && method.getName().equals("getTargetObject")
                    && method.getParameterTypes().length == 0;
        }

    }
}
