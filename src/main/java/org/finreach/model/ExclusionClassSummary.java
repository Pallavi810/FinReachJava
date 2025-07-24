package org.finreach.model;

import lombok.Data;

@Data
public class ExclusionClassSummary {
    private String location;
    private int maleExcludedCount;
    private int femaleExcludedCount;
    private int maleAtRiskCount;
    private int femaleAtRiskCount;

    public void update(String gender, String cls, long count) {
        if ("Male".equalsIgnoreCase(gender)) {
            if ("Excluded".equalsIgnoreCase(cls)) {
                this.maleExcludedCount += (int) count;
            } else if ("AtRisk".equalsIgnoreCase(cls)) {
                this.maleAtRiskCount += (int) count;
            }
        } else if ("Female".equalsIgnoreCase(gender)) {
            if ("Excluded".equalsIgnoreCase(cls)) {
                this.femaleExcludedCount += (int) count;
            } else if ("AtRisk".equalsIgnoreCase(cls)) {
                this.femaleAtRiskCount += (int) count;
            }
        }
    }
}