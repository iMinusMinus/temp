package ${package}.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Element implements Serializable {

    private String type;

    private String name;

    private String value;
}