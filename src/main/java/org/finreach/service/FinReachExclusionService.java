package org.finreach.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.*;
import org.finreach.model.AccountInfo;
import org.finreach.model.ExclusionClassSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FinReachExclusionService {
    @Autowired
    AccountInfoService accountInfoService;
    private final BigQuery bigquery;
    public FinReachExclusionService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/keyFile.json"));
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
                "    ARRAY(\n" +
                "      SELECT AS STRUCT cls, score\n" +
                "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "    ) AS class_scores\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                ")\n" +
                "WHERE EXISTS (\n" +
                "  SELECT 1\n" +
                "  FROM UNNEST(class_scores)\n" +
                "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                ")";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);

        return accountInfoService.convertTableResult(result);
    }

    public Long getCountExcludedAccountsByGender(String gender) throws InterruptedException {
        String query = "";
        if(gender.equalsIgnoreCase("Female")){
             query = "SELECT count(*)\n" +
                     "FROM (\n" +
                     "  SELECT\n" +
                     "    *,\n" +
                     "    ARRAY(\n" +
                     "      SELECT AS STRUCT cls, score\n" +
                     "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                     "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                     "      ON cls_offset = score_offset\n" +
                     "    ) AS class_scores\n" +
                     "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                     ")\n" +
                     "WHERE EXISTS (\n" +
                     "  SELECT 1\n" +
                     "  FROM UNNEST(class_scores)\n" +
                     "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                     ")\n" +
                     "And Gender= \"Female\";";
        }
        else {
           query="SELECT count(*)\n" +
                   "FROM (\n" +
                   "  SELECT\n" +
                   "    *,\n" +
                   "    ARRAY(\n" +
                   "      SELECT AS STRUCT cls, score\n" +
                   "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                   "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                   "      ON cls_offset = score_offset\n" +
                   "    ) AS class_scores\n" +
                   "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                   ")\n" +
                   "WHERE EXISTS (\n" +
                   "  SELECT 1\n" +
                   "  FROM UNNEST(class_scores)\n" +
                   "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                   ")\n" +
                   "And Gender= \"Male\";";
        }

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        return result.iterateAll().iterator().next().get(0).getLongValue();

    }

    public Long getCountExclusionAccountsByAge(Integer minAge, Integer maxAge) throws InterruptedException {


          String  query="SELECT count(*)\n" +
                  "FROM (\n" +
                  "  SELECT\n" +
                  "    *,\n" +
                  "    ARRAY(\n" +
                  "      SELECT AS STRUCT cls, score\n" +
                  "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                  "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                  "      ON cls_offset = score_offset\n" +
                  "    ) AS class_scores\n" +
                  "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                  ")\n" +
                  "WHERE EXISTS (\n" +
                  "  SELECT 1\n" +
                  "  FROM UNNEST(class_scores)\n" +
                  "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                  ")\n" +
                  "And  CAST(age AS INT64) BETWEEN @minAge AND @maxAge;";


        QueryJobConfiguration config = QueryJobConfiguration.newBuilder(query)
                .addNamedParameter("minAge", QueryParameterValue.int64(minAge))
                .addNamedParameter("maxAge", QueryParameterValue.int64(maxAge))
                .build();


        TableResult result = bigquery.query(config);
        return result.iterateAll().iterator().next().get(0).getLongValue();

    }
    public Map<String, Long> getCountExclusionAccountsByOccupation() throws InterruptedException {

         String   query="SELECT CustomerOccupation, count(*) AS occupationcount\n" +
                 "FROM (\n" +
                 "  SELECT\n" +
                 "    *,\n" +
                 "    ARRAY(\n" +
                 "      SELECT AS STRUCT cls, score\n" +
                 "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                 "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                 "      ON cls_offset = score_offset\n" +
                 "    ) AS class_scores\n" +
                 "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                 ")\n" +
                 "WHERE EXISTS (\n" +
                 "  SELECT 1\n" +
                 "  FROM UNNEST(class_scores)\n" +
                 "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                 ")\n" +
                 "GROUP BY CustomerOccupation;";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("CustomerOccupation").isNull() ? "Unknown" : row.get("CustomerOccupation").getStringValue();
            Long count = row.get("occupationcount").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }
    public Map<String, Long> getCountExclusionAccountsByLocation() throws InterruptedException {

        String   query="SELECT Location, count(*) as locationcount\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    *,\n" +
                "    ARRAY(\n" +
                "      SELECT AS STRUCT cls, score\n" +
                "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "    ) AS class_scores\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                ")\n" +
                "WHERE EXISTS (\n" +
                "  SELECT 1\n" +
                "  FROM UNNEST(class_scores)\n" +
                "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                ")\n" +
                "GROUP BY Location;";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("Location").isNull() ? "Unknown" : row.get("Location").getStringValue();
            Long count = row.get("locationcount").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }


    public Map<String, Long> getCountExcludedAccountsByMonthAndYear() throws InterruptedException {

        String   query="SELECT FORMAT_DATE('%Y-%m', DATE(TransactionDate)) AS month,\n" +
                "  COUNT(*) AS predicted_excluded_or_atrisk\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    *,\n" +
                "    ARRAY(\n" +
                "      SELECT AS STRUCT cls, score\n" +
                "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "    ) AS class_scores\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                ")\n" +
                "WHERE EXISTS (\n" +
                "  SELECT 1\n" +
                "  FROM UNNEST(class_scores)\n" +
                "  WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                ")\n" +
                "GROUP BY month\n" +
                "ORDER BY month;";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, Long> occupationMap = new LinkedHashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String occupation = row.get("month").isNull() ? "Unknown" : row.get("month").getStringValue();
            Long count = row.get("predicted_excluded_or_atrisk").getLongValue();
            occupationMap.put(occupation, count);
        }
        return occupationMap;

    }
    public List<ExclusionClassSummary> getExclusionByLocationAndGender() throws InterruptedException {

        String   query="SELECT\n" +
                "  Location,\n" +
                "  Gender,\n" +
                "  cls,\n" +
                "  COUNT(*) AS class_count\n" +
                "FROM (\n" +
                "  SELECT\n" +
                "    Location,\n" +
                "    Gender,\n" +
                "    (\n" +
                "      SELECT cls\n" +
                "      FROM UNNEST(predicted_ExclusionStatus.classes) AS cls WITH OFFSET AS cls_offset\n" +
                "      JOIN UNNEST(predicted_ExclusionStatus.scores) AS score WITH OFFSET AS score_offset\n" +
                "      ON cls_offset = score_offset\n" +
                "      WHERE (cls = \"Excluded\" OR cls = \"AtRisk\") AND score > 0.01\n" +
                "      LIMIT 1\n" +
                "    ) AS cls\n" +
                "  FROM `concrete-flight-466607-e5.FinReach.predictions_2025_07_22T00_19_24_475Z_105`\n" +
                ")\n" +
                "WHERE cls IS NOT NULL\n" +
                "GROUP BY Location, Gender, cls\n" +
                "ORDER BY Location, Gender, cls;";




        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result = bigquery.query(queryConfig);
        Map<String, ExclusionClassSummary> summaryMap = new HashMap<>();

        for (FieldValueList row : result.iterateAll()) {
            String location = row.get("Location").getStringValue();
            String gender = row.get("Gender").getStringValue();
            String cls = row.get("cls").getStringValue();
            long count = row.get("class_count").getLongValue();

            ExclusionClassSummary summary = summaryMap.computeIfAbsent(location, loc -> {
                ExclusionClassSummary s = new ExclusionClassSummary();
                s.setLocation(loc);
                return s;
            });

            summary.update(gender, cls, count);
        }

        return new ArrayList<>(summaryMap.values());


    }
}