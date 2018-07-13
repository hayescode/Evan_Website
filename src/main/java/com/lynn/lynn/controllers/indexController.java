package com.lynn.lynn.controllers;

import com.lynn.lynn.models.Data.UserDAO;
import com.lynn.lynn.models.Forms.LoginForm;
import com.lynn.lynn.models.Forms.SignUpForm;
import com.lynn.lynn.models.User.PasswordUtils;
import com.lynn.lynn.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Controller
public class indexController {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        return "index.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add Images");
        return "add.html";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("title", "Log In");
        return "login.html";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(Model model, @ModelAttribute @Valid LoginForm form, Errors errors) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login.html";
        }

        User user = userDAO.findIdByUsername(form.getUsername());
        String[] userPasswordParts = user.getPassword().split(",");
        String userHash = userPasswordParts[0];
        String userSalt = userPasswordParts[1];
        String loginPassword = PasswordUtils.generateSecurePassword(form.getPassword(),userSalt);

        if(user != null && loginPassword.equals(userHash)) {
            model.addAttribute("test", loginPassword);
            return "login.html";
        }

//        if (user != null && user.getPassword().equals(form.getPassword())) {
//            model.addAttribute("test", salt);
//        }
        model.addAttribute("title", "Log In");
        return "login.html";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String displaySignup(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("signUpForm", new SignUpForm());
        return "signup.html";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String processSignup(Model model, @ModelAttribute @Valid SignUpForm user, Errors errors) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Sign Up");
            model.addAttribute("errors", errors);
            return "signup.html";
        } else {
            if(user.getPassword().equals(user.getVerify())) {
                User newUser = new User(user.getUsername(), user.getEmail(), user.getPassword());
                userDAO.save(newUser);
                model.addAttribute("test", "Success!");
                return "signup.html";
            } else {
                model.addAttribute("title","Sign Up");
                return "signup.html";
            }
        }
    }
}
