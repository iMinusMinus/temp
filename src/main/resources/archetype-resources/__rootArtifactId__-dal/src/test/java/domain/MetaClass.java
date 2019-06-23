package ${package}.domain;


import java.util.List;

#if($frameworks.contains("jpa") and $config.contains("@"))
import  javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
#end

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author iMinusMinus
 * @date 2019-04-25
 */
@Getter
@Setter
@ToString
#if($frameworks.contains("jpa") and $config.contains("@"))
@Entity
@Table(name = "meta_class")
#end
public class MetaClass extends AbstractDomain {

    /**
     * class name.
     */
#if($frameworks.contains("jpa") and $config.contains("@"))
    @Column(name = "class_name", nullable = false, length = 65535)
#end
    private String name;

    /**
     * super class name
     */
#if($frameworks.contains("jpa") and $config.contains("@"))
    @Column(name = "super_class_name", length = 255)
#end
    private String superName;

#if($frameworks.contains("jpa") and $config.contains("@"))
    @OneToMany
    @JoinColumn(name = "class_name")
#end
    private List<MetaField> fields;

}