package ${package};

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DalContextConfig.class, DalConfig.class})
@ActiveProfiles(value = {"junit"})
public class DalTest {

#if($framework.contains('mybatis'))
    @Test
    public void testMyBatisConfig() {

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