package ${package}.dao;

import java.util.List;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ${package}.domain.MetaClass;

@Repository
public class MetaClassDaoImpl /* extends HibernateDaoSupport */ implements MetaClassMapper {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public long save(MetaClass domain) {
        sessionFactory.getCurrentSession().save(domain);## No Transaction, no session
        return domain.getId();
    }

    @Override
    @Transactional
    public int delete(long id) {
        ## sessionFactory.getCurrentSession().delete(entity); 按id删除，但非空列需赋值；打印SQL为update meta_class set class_name = ? where class_name = ?
        sessionFactory.getCurrentSession().createQuery("delete MetaClass m where m.id = " + id).executeUpdate();
        return 0;
    }

    @Override
    @Transactional
    public int update(MetaClass newValue, MetaClass oldValue) {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public MetaClass findOne(MetaClass condition) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public int count(MetaClass condition) {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetaClass> query(MetaClass condition, int offset, int limit) {
        return null;
    }
}