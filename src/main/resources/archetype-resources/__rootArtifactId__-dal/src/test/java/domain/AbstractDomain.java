package ${package}.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

#if($frameworks.contains("jpa") and $config.contains("@"))
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
#end

#if($frameworks.contains("jpa") and $config.contains("@"))
@MappedSuperclass
#end
@Getter
@Setter
@ToString
public abstract class AbstractDomain implements Serializable {

#if($frameworks.contains("jpa") and $config.contains("@"))
    @Id
#end
    protected long id;

}