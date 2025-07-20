package org.finreach.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
@NoArgsConstructor
public class FinReachService {
    public void getTableData() throws InterruptedException, IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:/Users/palla/FinReach/src/main/resources/phrasal-insight-465612-n2-24c5c97c49d9.json"));
        BigQuery bigquery = BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("phrasal-insight-465612-n2")
                .setLocation("us-central1")
                .build()
                .getService();


        String query = "SELECT * FROM `phrasal-insight-465612-n2.PallaviDataSet.predictions_2025_07_17T07_26_51_416Z_917` LIMIT 100";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);

        for (FieldValueList row : result.iterateAll()) {
            System.out.println(row);
        }
    }

    public void getAllFemaleDormantAccounts() throws InterruptedException, IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:/Users/palla/FinReach/src/main/resources/phrasal-insight-465612-n2-24c5c97c49d9.json"));
        BigQuery bigquery = BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("phrasal-insight-465612-n2")
                .setLocation("us-central1")
                .build()
                .getService();


        String query = "SELECT * FROM `phrasal-insight-465612-n2.PallaviDataSet.predictions_2025_07_17T07_26_51_416Z_917` where Gender='Female' LIMIT 1000";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);

        for (FieldValueList row : result.iterateAll()) {
            System.out.println(row);
        }
    }
}