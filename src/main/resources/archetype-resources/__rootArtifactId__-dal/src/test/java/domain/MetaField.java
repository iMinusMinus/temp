package ${package}.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
     * field value format, convert between string and {@code type}
     */
    private String format;

    /**
     * transient, readable, wrieable
     */
    private int flag;
}