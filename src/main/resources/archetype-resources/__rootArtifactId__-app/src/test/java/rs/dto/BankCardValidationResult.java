package ${package}.rs.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BankCardValidationResult implements Serializable {

    private String cardType;

    private String bank;

    private String key;

    private List<String> messages;

    private Boolean validated;

    private String stat;
}