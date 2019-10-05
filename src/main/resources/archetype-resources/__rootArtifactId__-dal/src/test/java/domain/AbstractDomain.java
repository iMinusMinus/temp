package ${package}.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

#if($framework.contains("jpa") and $configType.contains("@"))
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
#end

#if($framework.contains("jpa") and $configType.contains("@"))
@MappedSuperclass
#end
@Getter
@Setter
@ToString
public abstract class AbstractDomain implements Serializable {

#if($framework.contains("jpa") and $configType.contains("@"))
    @Id
#end
    protected long id;

}