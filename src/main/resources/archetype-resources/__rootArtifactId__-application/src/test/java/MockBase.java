package ${package};

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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