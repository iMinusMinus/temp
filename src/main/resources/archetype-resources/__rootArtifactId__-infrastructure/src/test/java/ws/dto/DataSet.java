package ${package}.ws.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"any"})
public class DataSet implements Serializable {

    @XmlAnyElement(lax = true)
    protected Object any;

}