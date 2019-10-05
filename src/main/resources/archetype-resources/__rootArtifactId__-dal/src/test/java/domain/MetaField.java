package ${package}.domain;

import java.io.Serializable;

#if($framework.contains("jpa") and $configType.contains("@"))
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
#end

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
#if($framework.contains("jpa") and $configType.contains("@"))
@Entity
@Table(name = "meta_field")
#end
public class MetaField extends AbstractDomain {

    /**
     * @see MetaClass.name
     */
    private String className;

    /**
     * field name
     */
    private String name;

    /**
     * field type
     */
    private String type;

    /**
     * field order
     */
    private int order;

    /**
     * field value format, convert between string and {@code type}
     */
    private String format;

    /**
     * transient, readable, wrieable
     */
    private int flag;
}