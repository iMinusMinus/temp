package ${package}.domain;


import java.util.List;

#if($framework.contains("jpa") and $configType.contains("@"))
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
#end

#if($framework.contains('mybatis') and !$configType.contains('xml'))
import org.apache.ibatis.type.Alias;
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
#if($framework.contains("jpa") and $configType.contains("@"))
@Entity
@Table(name = "meta_class")
#end
#if($framework.contains('mybatis') and !$configType.contains('xml'))
## // Optional. default alias: getClass().getSimpleName()
@Alias("metaClass")
#end
public class MetaClass extends AbstractDomain {

    /**
     * class name.
     */
#if($framework.contains("jpa") and $configType.contains("@"))
    @Column(name = "class_name", nullable = false, length = 65535)
#end
    private String name;

    /**
     * super class name
     */
#if($framework.contains("jpa") and $configType.contains("@"))
    @Column(name = "super_class_name", length = 255)
#end
    private String superName;

#if($framework.contains("jpa") and $configType.contains("@"))
    @OneToMany
    @JoinColumn(name = "class_name")
#end
    private List<MetaField> fields;

}