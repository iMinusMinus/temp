package ${package};

import ${package}.dao.MetaClassDaoImpl;
import ${package}.dao.MetaClassMapper;
import ${package}.domain.MetaClass;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional("transactionManager")
public class DalTest extends ContainerBase {

#if($framework.contains('mybatis'))
    @Resource
    private MetaClassMapper metaClassMapper;
#end

#if($framework.contains('hibernate'))
    @Resource
    private MetaClassMapper metaClassDaoImpl;
#end

#if($framework.contains('mybatis'))
    @Test
    public void testMyBatisConfig() {
        MetaClass mc = new MetaClass();
        mc.setName("java.beans.Bean");
        long effect = metaClassMapper.save(mc);
        Assert.assertEquals(1, effect);
        metaClassMapper.delete(mc.getId());
    }
#end
#if($framework.contains('hibernate'))

    @Test
    public void testHibernateConfig() {
        MetaClass mc = new MetaClass();
        mc.setName("java.beans.BeanDescriptor");
        metaClassDaoImpl.save(mc);
        metaClassDaoImpl.delete(mc.getId());
    }
#end

    @Test
    @Rollback
    @Repeat(2)
    public void testTransaction() {
        // assert first. then run SQL, finally test framework rollback
        MetaClass mc = new MetaClass();
        mc.setName("java.beans.BeanInfo");
#if($framework.contains('mybatis'))
        int effect = metaClassMapper.count(mc);
#elseif($framework.contains('hibernate'))
        int effect metaClassDaoImpl.count(mc);
#end
        Assert.assertEquals(0, effect);

#if($framework.contains('mybatis'))
        metaClassMapper.save(mc);
#elseif($framework.contains('hibernate'))
        metaClassDaoImpl.save(mc);
#end
    }
}