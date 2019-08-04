package ${package}.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Form implements Serializable {

    private String name;

    private List<Element> elements;

}