package org.finreach.web;

import org.finreach.model.AccountInfo;
import org.finreach.service.FinReachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class FinReachDormantController {

    @Autowired
    FinReachService finReachService;

        @GetMapping("getAllDormantAccounts")
        @ResponseBody
        public List<AccountInfo> getAllDormantAccounts() throws InterruptedException, IOException {
            // Replace Object with the actual return type of getTableData
             return finReachService.getTableData();
            //return ResponseEntity.ok(null);
        }

    @GetMapping("countDormantAccountsByGender")
    @ResponseBody
    public Long countDormantAccountsByGender(@RequestParam String gender) throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachService.getCountDormantAccountsByGender(gender);

    }

    @GetMapping("countDormantAccountsByAge")
    @ResponseBody
    public Long countDormantAccountsByAge(@RequestParam Integer minAge,@RequestParam Integer maxAge) throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachService.getCountDormantAccountsByAge(minAge, maxAge);

    }

    @GetMapping("countDormantAccountsByOccupation")
    @ResponseBody
    public Map<String, Long> countDormantAccountsByOccupation() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachService.getCountDormantAccountsByOccupation();

    }

    @GetMapping("countDormantAccountsByLocation")
    @ResponseBody
    public Map<String, Long> countDormantAccountsByLocation() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachService.getCountDormantAccountsByLocation();

    }

    @GetMapping("countDormantByMonthAndYear")
    @ResponseBody
    public Map<String, Long> countDormantByMonthAndYear() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        return finReachService.getCountDormantAccountsByMonthAndYear();

    }





}
