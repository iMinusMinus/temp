package ${package};

import org.junit.Before;
#if($configType.contains('@java'))
import org.junit.runner.RunWith;
#end
import org.junit.Test;

#if(!$configType.contains('@java'))
import org.mockito.MockitoAnnotations;
#else
import org.mockito.junit.MockitoJUnitRunner;
#end

/**
 * base mock class for unit test
 *
 * @author iMinusMinus
 * @date 2019-04-30
 */
#if($configType.contains('@java'))
@RunWith(MockitoJUnitRunner.class)
#end
public abstract class MockBase {

#if(!$configType.contains('@java'))
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
#end

}