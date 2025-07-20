package org.finreach.web;

import org.finreach.service.FinReachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class FinReachController {

    @Autowired
    FinReachService finReachService;

        @GetMapping("getAllDormantAccounts")
        @ResponseBody
        public ResponseEntity<?> getAllDormantAccounts() throws InterruptedException, IOException {
            // Replace Object with the actual return type of getTableData
             finReachService.getTableData();
            return ResponseEntity.ok(null);
        }

    @GetMapping("getAllFemaleDormantAccounts")
    @ResponseBody
    public ResponseEntity<?> getTableData() throws InterruptedException, IOException {
        // Replace Object with the actual return type of getTableData
        finReachService.getAllFemaleDormantAccounts();
        return ResponseEntity.ok(null);
    }



}
