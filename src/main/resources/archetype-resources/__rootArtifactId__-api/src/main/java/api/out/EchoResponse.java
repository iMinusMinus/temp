package ${package}.api.out;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * echo response
 *
 * @author <a href="mailto:mean.leung@outlook.com">Mean.Leung</a>
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class EchoResponse implements Serializable {

    private static final long SerialVersionUUID = 1L;

    /**
     * @see ${package}.constant.EchoStatus
     */
    private int status;

    /**
     * maybe sometime works
     */
    private Date date;

}