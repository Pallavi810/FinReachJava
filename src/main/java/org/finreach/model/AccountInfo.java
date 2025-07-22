package org.finreach.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountInfo implements Serializable {

    @JsonProperty("accountID")
    private String accountID;

    @JsonProperty("accountHolderName")
    private String accountHolderName;

    @JsonProperty("accountBalance")
    private BigDecimal accountBalance;

    @JsonProperty("accountOpeningDate")
    private LocalDate accountOpeningDate;

    @JsonProperty("age")
    private int age;

    @JsonProperty("customerOccupation")
    private String customerOccupation;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("merchantType")
    private String merchantType;

    @JsonProperty("transactionDate")
    private LocalDate transactionDate;
    @JsonProperty("transactionType")
    private String transactionType;
}