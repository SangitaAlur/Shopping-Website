package com.spring.data.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.spring.data.entity.User;
import com.spring.data.service.CartService;
import com.spring.data.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
@SessionAttributes("cartCount")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpSession session, Model model) {
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            session.setAttribute("user", loggedInUser);
            return "redirect:/welcome";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/welcome")
    public String showWelcomePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        int cartCount = cartService.getCartCount(user.getId());
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("user", user);
        return "welcome";
    } 

      @GetMapping("/logout")
      public String logout(HttpServletRequest request) {
          HttpSession session = request.getSession();
          session.invalidate();
          return "redirect:/login?logout";
      }
}
