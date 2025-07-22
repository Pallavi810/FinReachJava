package org.finreach.service;

import com.google.cloud.bigquery.FieldValueList;
import org.finreach.model.AccountInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface AccountInfoMapper {

    AccountInfoMapper INSTANCE = Mappers.getMapper(AccountInfoMapper.class);

    @Mapping(target = "accountID", expression = "java(row.get(\"accountID\").isNull() ? null : row.get(\"accountID\").getStringValue())")
    @Mapping(target = "accountHolderName", expression = "java(row.get(\"accountHolderName\").isNull() ? null : row.get(\"accountHolderName\").getStringValue())")
    @Mapping(target = "accountBalance", expression = "java(row.get(\"accountBalance\").isNull() ? null : new java.math.BigDecimal(row.get(\"accountBalance\").getStringValue()))")
    @Mapping(target = "accountOpeningDate", source = "row", qualifiedByName = "mapAccountOpeningDate")
    @Mapping(target = "transactionDate", source = "row", qualifiedByName = "mapTransactionDate")
    @Mapping(target = "transactionType", expression = "java(row.get(\"transactionType\").isNull() ? null : row.get(\"transactionType\").getStringValue())")
    @Mapping(target = "age", expression = "java(row.get(\"age\").isNull() ? 0 : (int) row.get(\"age\").getLongValue())")
    @Mapping(target = "customerOccupation", expression = "java(row.get(\"customerOccupation\").isNull() ? null : row.get(\"customerOccupation\").getStringValue())")
    @Mapping(target = "gender", expression = "java(row.get(\"gender\").isNull() ? null : row.get(\"gender\").getStringValue())")
    @Mapping(target = "merchantType", expression = "java(row.get(\"merchantType\").isNull() ? null : row.get(\"merchantType\").getStringValue())")
    AccountInfo fromRow(FieldValueList row);

    @Named("mapAccountOpeningDate")
    default LocalDate mapAccountOpeningDate(FieldValueList row) {
        if (row.get("Account_Opening_Date").isNull()) return null;
        return LocalDate.parse(row.get("Account_Opening_Date").getStringValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Named("mapTransactionDate")
    default LocalDate mapTransactionDate(FieldValueList row) {
        if (row.get("TransactionDate").isNull()) return null;
        return LocalDate.parse(row.get("TransactionDate").getStringValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
