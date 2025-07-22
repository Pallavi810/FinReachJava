package org.finreach.service;

import com.google.cloud.bigquery.TableResult;
import com.google.cloud.bigquery.FieldValueList;
import org.finreach.model.AccountInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class AccountInfoService {

    private final AccountInfoMapper mapper = AccountInfoMapper.INSTANCE;

    public List<AccountInfo> convertTableResult(TableResult result) {
        return StreamSupport.stream(result.iterateAll().spliterator(), false)
                .map(mapper::fromRow)
                .collect(Collectors.toList());
    }

}
