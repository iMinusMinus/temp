package ${package}.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

#if($framework.contains("jpa") and $configType.contains("@"))
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
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
    @SequenceGenerator(name = "pg_meta_class_seq", sequenceName = "meta_class_id_seq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pg_meta_class_seq")## SEQUENCE: PsotgreSQL/Oracle, IDENTITY: MySQL
#end

    protected long id;

}