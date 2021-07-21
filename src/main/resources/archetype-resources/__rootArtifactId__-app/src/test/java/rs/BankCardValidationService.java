package ${package}.rs;

import ${package}.rs.dto.BankCardValidationResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface BankCardValidationService {

    @GET
    @Path("/validateAndCacheCardInfo.json")
    BankCardValidationResult validate(@QueryParam("cardNo") String cardNo, @QueryParam("cardBinCheck") boolean cardBinCheck);
}