package org.finreach.model;

import lombok.Data;

@Data
public class DormantSummary {
    private String location;
    private int maleDormantCount;
    private int femaleDormantCount;

    public void addCount(String gender, long count) {
        if ("Male".equalsIgnoreCase(gender)) {
            this.maleDormantCount += (int) count;
        } else if ("Female".equalsIgnoreCase(gender)) {
            this.femaleDormantCount += (int) count;
        }
    }
}