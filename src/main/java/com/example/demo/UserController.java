package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // After each change we redirect to the home page with ?msg=...
    // index.html reads it and shows a small confirmation toast.

    @PostMapping("/add")
    public String addUser(@RequestParam String name, @RequestParam String email) {
        User user = new User(name, email);
        userService.saveUser(user);
        return "redirect:/?msg=added";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long deleteId) {
        if (userService.getUserById(deleteId) == null) {
            return "redirect:/?msg=notfound";
        }
        userService.deleteUser(deleteId);
        return "redirect:/?msg=deleted";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long updateId, @RequestParam String updateName) {
        User user = userService.getUserById(updateId);
        if (user == null) {
            return "redirect:/?msg=notfound";
        }
        user.setName(updateName);
        userService.saveUser(user);
        return "redirect:/?msg=updated";
    }

    @GetMapping("/get")
    public String getUser(@RequestParam Long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/getallusers")
    public String getUsers(Model model) {
        List<User> users = userService.getallUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/usersusingpagination")
    public String getUsersusingPagination(Model model, Pageable pageable) {
        int pageSize = 5; // Specify the number of rows per page here
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        Page<User> usersPage = userService.getUsersusingPagination(pageable);
        model.addAttribute("usersPage", usersPage);
        return "usersbyPagination";
    }
}
