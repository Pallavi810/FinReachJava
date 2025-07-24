package org.finreach.web;

import org.finreach.model.AccountInfo;
import org.finreach.model.ExclusionClassSummary;
import org.finreach.service.FinReachExclusionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class FinReachExclusionController {

    @Autowired
    FinReachExclusionService finReachExclusionService;

        @GetMapping("getAllExcludedAccounts")
        @ResponseBody
        public List<AccountInfo> getAllDormantAccounts() throws InterruptedException, IOException {
            // Replace Object with the actual return type of getTableData
             return finReachExclusionService.getTableData();
            //return ResponseEntity.ok(null);
        }

    @GetMapping("countExcludedAccountsByGender")
    @ResponseBody
    public Long countDormantAccountsByGender(@RequestParam String gender) throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getCountExcludedAccountsByGender(gender);

    }

    @GetMapping("countExcludedAccountsByAge")
    @ResponseBody
    public Long countDormantAccountsByAge(@RequestParam Integer minAge,@RequestParam Integer maxAge) throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getCountExclusionAccountsByAge(minAge, maxAge);

    }

    @GetMapping("countExcludedAccountsByOccupation")
    @ResponseBody
    public Map<String, Long> countExclusionAccountsByOccupation() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getCountExclusionAccountsByOccupation();

    }

    @GetMapping("countExcludedAccountsByLocation")
    @ResponseBody
    public Map<String, Long> countDormantAccountsByLocation() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getCountExclusionAccountsByLocation();

    }

    @GetMapping("countExcludedByMonthAndYear")
    @ResponseBody
    public Map<String, Long> countExcludedByMonthAndYear() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getCountExcludedAccountsByMonthAndYear();

    }

    @GetMapping("countExclusionLocationGender")
    @ResponseBody
    public List<ExclusionClassSummary> countExclusionLocationGender() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachExclusionService.getExclusionByLocationAndGender();

    }





}
