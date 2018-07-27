package com.lynn.lynn.controllers;

import com.lynn.lynn.models.Category.Category;
import com.lynn.lynn.models.Data.CategoryDAO;
import com.lynn.lynn.models.Data.ImagesDAO;
import com.lynn.lynn.models.Data.UserDAO;
import com.lynn.lynn.models.Forms.LoginForm;
import com.lynn.lynn.models.Forms.SignUpForm;
import com.lynn.lynn.models.Images.Images;
import com.lynn.lynn.models.Images.fileImages;
import com.lynn.lynn.models.User.PasswordUtils;
import com.lynn.lynn.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Optional;


@Controller
public class indexController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ImagesDAO imagesDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("fileCount", imagesDAO.count());
        model.addAttribute("images", imagesDAO.findAll());
        return "index.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("title", "Add Images");
        model.addAttribute("categories", categoryDAO.findAll());
        return "add.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAdd(@RequestParam("file")MultipartFile file,
                             @RequestParam("category") int categoryId) {
        //Add files to file system
        String fileName = file.getOriginalFilename();
        File save = new File("C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\" + fileName);
        try {
            if (!file.isEmpty()) {
                BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                //TODO change path once on server
                File destination = new File("C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\" + fileName); // something like C:/Users/tom/Documents/nameBasedOnSomeId.png
                ImageIO.write(src, "jpg", destination);
            }
        } catch (Exception e) {
            System.out.println("Exception occured" + e.getMessage());
        }

        //Add file to SQL database (table = images)
        String path = "/images/" + fileName;
        Images newImage = new Images(path);
        Optional<Category> oCat = categoryDAO.findById(categoryId);
        Category cat = oCat.get();
        newImage.setCategory(cat);
        imagesDAO.save(newImage);
        return "index.html";
    }

    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    public String processAddCategory(@RequestParam("category") String category, Model model) {
        Category newCategory = new Category(category);
        categoryDAO.save(newCategory);
        model.addAttribute("title", "Add Images");
        return "redirect:/add";
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
