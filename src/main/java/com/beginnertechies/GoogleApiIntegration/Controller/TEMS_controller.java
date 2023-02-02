package com.beginnertechies.GoogleApiIntegration.Controller;


import com.beginnertechies.GoogleApiIntegration.Service.TEMS_employeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TEMS_controller {

    @Autowired
    TEMS_employeeServiceImpl googleApiService;

    @GetMapping("/check")
    public String check(){
        return "Checking API!!";
    }

    @GetMapping("/getData")
    public String getData(){
//        return googleApiService.readDataFromGoogleSheet();
        return null;
    }
}
