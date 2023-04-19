package kz.bitlab.javapro.security.springsecuritydemo.api;

import kz.bitlab.javapro.security.springsecuritydemo.dto.NewsDTO;
import kz.bitlab.javapro.security.springsecuritydemo.model.News;
import kz.bitlab.javapro.security.springsecuritydemo.model.Users;
import kz.bitlab.javapro.security.springsecuritydemo.services.NewsService;
import kz.bitlab.javapro.security.springsecuritydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @GetMapping(value = "/")
    public String indexPage(Model model){
        return "index";
    }

    @GetMapping(value = "/enter")
    public String enterPage(Model model){
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model){
        return "register";
    }

    @PostMapping(value = "/toregister")
    public String toRegister(@RequestParam(name = "user-email") String userEmail,
                             @RequestParam(name = "user-password") String password,
                             @RequestParam(name = "user-full-name") String fullName,
                             @RequestParam(name = "user-re-password") String rePassword){

        if(password.equals(rePassword)){
            Users newUser = new Users();
            newUser.setEmail(userEmail);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser = userService.registerUser(newUser);
            if(newUser!=null){
                return "redirect:/register?success";
            }
        }

        return "redirect:/register?error";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatepassword")
    public String updatePassword(@RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "re_new_password") String reNewPassword){
        if(newPassword.equals(reNewPassword)){
            Users updatedPassword = userService.updatePassword(getCurrentUser(), oldPassword, newPassword);
            if(updatedPassword!=null){
                return "redirect:/profile?success";
            }
        }
        return "redirect:/profile?error";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String profilePage(Model model){
        return "profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/adminpanel")
    public String adminPage(Model model){
        return "admin";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @GetMapping(value = "/teacherpanel")
    public String teacherPage(Model model){
        return "teacher";
    }

    @GetMapping(value = "/forbidden")
    public String forbiddenPage(Model model){
        return "forbidden";
    }

    private Users getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user = (Users) authentication.getPrincipal();
            return user;
        }
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/news")
    public String newsPage(Model model){
        List<NewsDTO> newsList = newsService.getAllNews();
        model.addAttribute("newsList",newsList);
        return "news";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/addnews")
    public String addNews(@RequestParam(name = "title") String title,
                          @RequestParam(name = "content") String content){

        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setPostDate(new Date());
        news.setAuthor(getCurrentUser());
        newsService.addNews(news);
        return "redirect:/news";
    }
}
