package ${package}.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Form implements Serializable {

    @NotNull
    private String name;

    private List<Element> elements;

}