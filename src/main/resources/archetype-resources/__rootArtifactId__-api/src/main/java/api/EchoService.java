package ${package}.api;

import ${package}.api.in.EchoRequest;
import ${package}.api.out.EchoResponse;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    EchoResponse echo(EchoRequest request);

}