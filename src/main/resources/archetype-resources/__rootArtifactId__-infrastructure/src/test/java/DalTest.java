package ${package};

import ${package}.dao.MetaClassDaoImpl;
import ${package}.dao.MetaClassMapper;
import ${package}.domain.MetaClass;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

public class DalTest extends ContainerBase {

    @Resource
    private MetaClassMapper metaClassMapper;

    @Resource
    private MetaClassMapper metaClassDaoImpl;

#if($framework.contains('mybatis'))
    @Test
    public void testMyBatisConfig() {
        MetaClass mc = new MetaClass();
        mc.setName("java.beans.Bean");
        //XXX PostgreSQL serial bug?
        long id = metaClassMapper.save(mc);
        Assert.assertNotEquals(0, id);
        MetaClass qr = metaClassMapper.findOne(mc);
        //Assert.assertEquals(id, qr.getId());
        metaClassMapper.delete( qr.getId());
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
    public void testTransaction() {

    }
}