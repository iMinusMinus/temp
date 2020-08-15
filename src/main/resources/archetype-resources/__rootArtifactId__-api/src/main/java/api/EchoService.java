package ${package}.api;

import ${package}.api.in.EchoRequest;
import ${package}.api.out.EchoResponse;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * echo service
 *
 * @author <a href="mailto:mean.leung@outlook.com">Mean.Leung</a>
 * @version 1.0
 */
@Path("/test")
public interface EchoService {

    /**
     * echo test
     *
     * @param request echo request
     * @return echo response is echo request
     */
    @Path("/echo")
    @GET
    //No @Consumes or @Produces, content negotiation through http header
    EchoResponse echo(EchoRequest request);

}