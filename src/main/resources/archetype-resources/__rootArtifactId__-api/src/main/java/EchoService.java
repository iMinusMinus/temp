package ${package}.api;

import ${package}.in.EchoRequest;
import ${package}.out.EchoResponse;
/**
 * echo service
 *
 * @author <a href="mailto:mean.leung@outlook.com">Mean.Leung</a>
 * @version 1.0
 */
public interface EchoService {

    /**
     * echo test
     *
     * @param request echo request
     * @return echo response is echo request
     */
    EchoResponse echo(EchoRequest request);

}