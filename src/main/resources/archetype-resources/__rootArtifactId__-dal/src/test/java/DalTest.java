package ${package};

import ${package}.dao.MetaClassMapper;
import ${package}.domain.MetaClass;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DalContextConfig.class, DalConfig.class})
@ActiveProfiles(value = {"junit"})
public class DalTest {

    @Resource
    private MetaClassMapper metaClassDao;

#if($framework.contains('mybatis'))
    @Test
    public void testMyBatisConfig() {
        MetaClass mc = new MetaClass();
        mc.setName("java.beans.Bean");
        //XXX PostgreSQL serial bug?
        long id = metaClassDao.save(mc);
        Assert.assertNotEquals(0, id);
        MetaClass qr = metaClassDao.findOne(mc);
        //Assert.assertEquals(id, qr.getId());
        metaClassDao.delete( qr.getId());
    }
#elseif($framework.contains('hibernate'))

    @Test
    public void testHibernateConfig() {

    }
#end

    @Test
    @Rollback
    public void testTransaction() {

    }
}