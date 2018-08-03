package com.lynn.lynn.controllers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.lynn.lynn.Security.SecurityService;
import com.lynn.lynn.Security.UserService;
import com.lynn.lynn.Security.UserValidator;
import com.lynn.lynn.models.Category.Category;
import com.lynn.lynn.models.Data.CategoryDAO;
import com.lynn.lynn.models.Data.ImagesDAO;
import com.lynn.lynn.models.Data.UserDAO;
import com.lynn.lynn.models.Forms.LoginForm;
import com.lynn.lynn.models.Forms.SignUpForm;
import com.lynn.lynn.models.Images.Images;
import com.lynn.lynn.models.Images.RotateImage;
import com.lynn.lynn.models.User.PasswordUtils;
import com.lynn.lynn.models.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

@Controller
public class indexController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ImagesDAO imagesDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, Principal user) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("images", imagesDAO.findAll());
        model.addAttribute("categories", categoryDAO.findAll());
        if(user != null) {
            String name = user.getName();
            model.addAttribute("user",name);
        }
        return "index.html";
    }

    @RequestMapping(value = "/{cat_id}", method = RequestMethod.GET)
    public String filteredIndex(Model model, HttpServletRequest request, @PathVariable(value = "cat_id") int cat_id) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("images", imagesDAO.findByCategoryId(cat_id));
        model.addAttribute("categories", categoryDAO.findAll());
        return "index.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("title", "Add Images");
        model.addAttribute("categories", categoryDAO.findAll());
        return "add.html";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAdd(@RequestParam("files")MultipartFile[] files,
                             @RequestParam("category") int categoryId) {

        for(MultipartFile eachFile : files) {
            //Add each file to the file system
            String fileName = eachFile.getOriginalFilename();
            try {
                if (!eachFile.isEmpty()) {
                    //convert Multipart to file in order to get metadata
                    File convFile = new File(eachFile.getOriginalFilename());
                    convFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(convFile);
                    fos.write(eachFile.getBytes());
                    fos.close();

                    BufferedImage src = ImageIO.read(new ByteArrayInputStream(eachFile.getBytes()));

                    //get metadata, specifically orientation
                    Metadata metadata = ImageMetadataReader.readMetadata(convFile);
                    ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

                    int orientation = 1;
                    try {
                        orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    int width = src.getWidth();
                    int height = src.getHeight();
                    if (orientation == 3 || orientation == 6 || orientation == 8) {
                        AffineTransform affineTransform = RotateImage.getTransform(orientation, width, height);
                        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
                        BufferedImage destinationImage = new BufferedImage(height, width, src.getType());
                        destinationImage = affineTransformOp.filter(src, destinationImage);
                        ImageIO.write(destinationImage, "jpg", new File("C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\" + fileName));
                    } else {
                        ImageIO.write(src, "jpg", new File("C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\" + fileName));
                    }
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
        }
        return "redirect:/";
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
            model.addAttribute("errors", errors);
            model.addAttribute("title", "Log In");
            return "login.html";
        }

        User user = userDAO.findIdByUsername(form.getUsername());

        if(user == null) {
            model.addAttribute("error", "Username Not Found");
            model.addAttribute("title", "Log In");
            return "login.html";
        }

        String[] userPasswordParts = user.getPassword().split(",");
        String userHash = userPasswordParts[0];
        String userSalt = userPasswordParts[1];
        String loginPassword = PasswordUtils.generateSecurePassword(form.getPassword(),userSalt);

        if(!loginPassword.equals(userHash)) {
            model.addAttribute("error", "Incorrect Password");
            model.addAttribute("title", "Log In");
            return "login.html";
        }

        //session and persitent 'logged in' stuff here.

        model.addAttribute("title", "Log In");
        model.addAttribute("test", userHash);
        return "login.html";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String displaySignup(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("signUpForm", new User());
        return "signup.html";
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String processSignup(Model model, @ModelAttribute("signUpForm") User userForm, Errors errors) {

        userValidator.validate(userForm, errors);

        if(errors.hasErrors()) {
            return "signup.html";
        }

        userService.save(userForm);
        securityService.autologin(userForm.getUsername(),userForm.getPassword());
        model.addAttribute("signedIn", "Signed in as " + userForm.getUsername());

        return "redirect:";






        /*User foundUsername = userDAO.findIdByUsername(user.getUsername());
        if(errors.hasErrors()) {
            model.addAttribute("title", "Sign Up");
            model.addAttribute("errors", errors);
            return "signup.html";
        } else {
            if(user.getPassword().equals(user.getVerify()) && foundUsername.getUsername() != user.getUsername()) {
                User newUser = new User(user.getUsername(), user.getEmail(), user.getPassword());
                userDAO.save(newUser);
                model.addAttribute("test", "Success!");
                return "signup.html";
            } else {
                model.addAttribute("title","Sign Up");
                return "signup.html";
            }
        }*/
    }
}
