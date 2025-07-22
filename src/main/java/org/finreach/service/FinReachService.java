package org.finreach.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.finreach.model.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinReachService {
    @Autowired
    AccountInfoService accountInfoService;
    private final BigQuery bigquery;
    public FinReachService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:/Users/palla/FinReachJava/src/main/resources/servicekey.json"));
         bigquery = BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("concrete-flight-466607-e5")
                .setLocation("us-central1")
                .build()
                .getService();
    }
    public List<AccountInfo> getTableData() throws InterruptedException {
        String query = "SELECT *\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    *,\n" +
                "    (\n" +
                "      SELECT score\n" +
                "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "      WHERE cls = \"True\"\n" +
                "      LIMIT 1\n" +
                "    ) AS true_score\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                ")\n" +
                "WHERE true_score IS NOT NULL AND true_score > 0.5";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);

        return accountInfoService.convertTableResult(result);
    }

    public Long getCountDormantAccountsByGender(String gender) throws InterruptedException {
        String query = "";
        if(gender.equalsIgnoreCase("Female")){
             query = "SELECT COUNT(*) AS dormant_female_count\n" +
                     "FROM (\n" +
                     "  SELECT\n" +
                     "    Gender,\n" +
                     "    (\n" +
                     "      SELECT score\n" +
                     "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                     "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                     "      ON cls_offset = score_offset\n" +
                     "      WHERE cls = \"True\"\n" +
                     "      LIMIT 1\n" +
                     "    ) AS true_score\n" +
                     "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                     ")\n" +
                     "WHERE true_score IS NOT NULL AND true_score > 0.5 AND Gender = \"Female\"; ";
        }
        else {
           query="SELECT COUNT(*) AS dormant_female_count\n" +
                   "FROM (\n" +
                   "  SELECT\n" +
                   "    Gender,\n" +
                   "    (\n" +
                   "      SELECT score\n" +
                   "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                   "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                   "      ON cls_offset = score_offset\n" +
                   "      WHERE cls = \"True\"\n" +
                   "      LIMIT 1\n" +
                   "    ) AS true_score\n" +
                   "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                   ")\n" +
                   "WHERE true_score IS NOT NULL AND true_score > 0.5 AND Gender = \"Male\"; ";
        }

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        return result.iterateAll().iterator().next().get(0).getLongValue();

    }

    public Long getCountDormantAccountsByAge(Integer minAge,Integer maxAge) throws InterruptedException {
        String query = "SELECT COUNT(*) AS dormant_age_30_90_count\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    Age,\n" +
                "    (\n" +
                "      SELECT score\n" +
                "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "      WHERE cls = \"True\"\n" +
                "      LIMIT 1\n" +
                "    ) AS true_score\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                ")\n" +
                "WHERE true_score IS NOT NULL AND true_score > 0.5 AND CAST(age AS INT64) BETWEEN @minAge AND @maxAge";


        QueryJobConfiguration config = QueryJobConfiguration.newBuilder(query)
                .addNamedParameter("minAge", QueryParameterValue.int64(minAge))
                .addNamedParameter("maxAge", QueryParameterValue.int64(maxAge))
                .build();


        TableResult result = bigquery.query(config);
        return result.iterateAll().iterator().next().get(0).getLongValue();

    }
    public Map<String, Long> getCountDormantAccountsByOccupation() throws InterruptedException {

         String   query="SELECT\n" +
                 "  CustomerOccupation,\n" +
                 "  COUNT(*) AS dormant_count\n" +
                 "FROM (\n" +
                 "  SELECT\n" +
                 "    CustomerOccupation,\n" +
                 "    (\n" +
                 "      SELECT score\n" +
                 "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                 "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                 "      ON cls_offset = score_offset\n" +
                 "      WHERE cls = \"True\"\n" +
                 "      LIMIT 1\n" +
                 "    ) AS true_score\n" +
                 "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                 ")\n" +
                 "WHERE true_score IS NOT NULL AND true_score > 0.5\n" +
                 "GROUP BY CustomerOccupation\n" +
                 "ORDER BY dormant_count DESC";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("CustomerOccupation").isNull() ? "Unknown" : row.get("CustomerOccupation").getStringValue();
            Long count = row.get("dormant_count").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }
    public Map<String, Long> getCountDormantAccountsByLocation() throws InterruptedException {

        String   query="SELECT\n" +
                "  Location,\n" +
                "  COUNT(*) AS dormant_count\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    Location,\n" +
                "    (\n" +
                "      SELECT score\n" +
                "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "      WHERE cls = \"True\"\n" +
                "      LIMIT 1\n" +
                "    ) AS true_score\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                ")\n" +
                "WHERE true_score IS NOT NULL AND true_score > 0.5\n" +
                "GROUP BY Location\n" +
                "ORDER BY dormant_count DESC";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("Location").isNull() ? "Unknown" : row.get("Location").getStringValue();
            Long count = row.get("dormant_count").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }

    public Map<String, Long> getCountDormantAccountsByMonthAndYear() throws InterruptedException {

        String   query="SELECT\n" +
                "  FORMAT_DATE('%Y-%m', DATE(Account_Opening_Date)) AS month,\n" +
                "  COUNT(*) AS dormant_count\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    Account_Opening_Date,\n" +
                "    (\n" +
                "      SELECT score\n" +
                "      FROM UNNEST(predicted_IsDormant.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_IsDormant.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "      WHERE cls = \"True\"\n" +
                "      LIMIT 1\n" +
                "    ) AS true_score\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T10_23_26_330Z_365`\n" +
                ")\n" +
                "WHERE true_score IS NOT NULL AND true_score > 0.5\n" +
                "GROUP BY month\n" +
                "ORDER BY month";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("month").isNull() ? "Unknown" : row.get("month").getStringValue();
            Long count = row.get("dormant_count").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }
}