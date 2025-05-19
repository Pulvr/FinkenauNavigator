package com.example.FinkenauNavigator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LandingPageController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    String registerPage(){
        return "regis";
    }

    @PostMapping("/start")
    String startPage(Model model, @RequestParam("user") String name, @RequestParam("pWord") String pWord) {
        UserAccounts myUser = new UserAccounts(name, pWord);
        userRepository.save(myUser);

        if (!userRepository.findByName(name).isEmpty()){
            model.addAttribute("UserOrError",name);
        }else{
            model.addAttribute("UserOrError", "Error");
        }

        return "start";
    }
}
