package com.zoubair.lastusersproject.Controller;

import com.zoubair.lastusersproject.dto.UserDto;
import com.zoubair.lastusersproject.entities.Panne;
import com.zoubair.lastusersproject.entities.User;
import com.zoubair.lastusersproject.services.PanneService;
import com.zoubair.lastusersproject.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final PanneService panneService;

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(Model model){
        model.addAttribute("roles", getAuthenticationAuthorities());
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        return "index";
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("isLoginPage", true);
        return "index";
    }

    @GetMapping("/astuce")
    public String astuce(Model model){
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "astuce";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "2") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<User> users = userService.findAllUsers(keyword, PageRequest.of(page,size));
//        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users.getContent());
        model.addAttribute("pages", new int [users.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "users";
    }

    @GetMapping("/pannes-historique")
    public String getListPannes(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size",defaultValue = "2") int size,
                                @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<Panne> pannes = panneService.findAllPanneForClient(keyword, getUsername(), PageRequest.of(page,size));
        model.addAttribute("pannes", pannes.getContent());
        model.addAttribute("pages", new int [pannes.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "HistoriquepannesClient";
    }

    @GetMapping("/pannes-technicien")
    public String getListPannesTechnicien(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size",defaultValue = "2") int size,
                                @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<Panne> pannes = panneService.findAllPanneForTechnicien(keyword, getUsername(), PageRequest.of(page,size));
        model.addAttribute("pannes", pannes.getContent());
        model.addAttribute("pages", new int [pannes.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "pannesTechnicien";
    }

    @PostMapping("/assignPanne")
    public String assignPanne(@RequestParam Long panneId, @RequestParam Long technicienId) {
        Panne panne = panneService.findPanneById(panneId);
        User technicien = userService.findUserById(technicienId);
        panne.setAssignedTo(technicien);
        panne.setStatus("ASSIGNED");
        panneService.savePanne(panne);
        return "redirect:/pannes";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("isLoginPage", false);
        return "login";
    }

    @GetMapping("/profile")
    public String getProfile(Model model){
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("user") UserDto userDto,BindingResult result, Model model){
        model.addAttribute("user", userDto);
        userService.UpdateUser(userDto);
        return "redirect:/profile?success";
    }

    @GetMapping("/panne")
    public String showPanneForm(Model model){
        // create model object to store form data
        Panne panne = new Panne();
        model.addAttribute("panne", panne);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "panneDeclaration";
    }

    @PostMapping("/panne/save")
    public String createPanne(@Valid @ModelAttribute("panne") Panne panne,
                              BindingResult result,
                              Model model){
        panneService.createPanne(panne);
        return "redirect:/panne?success";
    }

    @GetMapping("/pannes")
    public String getHistoriquePannesClient(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size",defaultValue = "2") int size,
                                @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<Panne> pannes = panneService.findAllPanne(keyword, PageRequest.of(page,size));
        List<User> techniciens = userService.getAllTechniciens();
        model.addAttribute("pannes", pannes.getContent());
        model.addAttribute("techniciens", techniciens);
        model.addAttribute("pages", new int [pannes.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "pannes";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Long id, String keyword, int page){
        userService.deleteUser(id);
        return "redirect:/users?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editUser")
    public String editUser(Model model, Long id, String keyword, int page){
        User user = userService.findUserById(id);
        if(user == null)
            throw new RuntimeException("user not found");
        model.addAttribute("user", user);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "editUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(Model model,@Valid User user, BindingResult bindingResult,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors())
            return "editUser";
        User user1 = userService.findUserById(user.getId());
        user.setRoles(user1.getRoles());
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/users?page="+page+"&keyword="+keyword;
    }

    // handler method to handle user registration form request
    @GetMapping("/technicien")
    public String showCreateTechnicienForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "technicien";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/technicien/save")
    public String createTechnicien(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/technicien";
        }

        userService.saveTechnicien(userDto);
        return "redirect:/technicien?success";
    }

    @GetMapping("/techniciens")
    public String techniciens(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size",defaultValue = "2") int size,
                        @RequestParam(name = "keyword",defaultValue = "") String keyword){
        Page<User> techniciens = userService.findAllTechniciens(keyword, PageRequest.of(page,size));
//        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("techniciens", techniciens.getContent());
        model.addAttribute("pages", new int [techniciens.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "techniciens";
    }

    @GetMapping("/deleteTechnicien")
    public String deleteTechnicien(Long id, String keyword, int page){
        userService.deleteUser(id);
        return "redirect:/techniciens?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/editTechnicien")
    public String editTechnicien(Model model, Long id, String keyword, int page){
        User technicien = userService.findUserById(id);
        if(technicien == null)
            throw new RuntimeException("user not found");
        model.addAttribute("technicien", technicien);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("username", getUsername());
        model.addAttribute("isLoginPage", true);
        model.addAttribute("roles", getAuthenticationAuthorities());
        return "editTechnicien";
    }

    @PostMapping("/saveTechnicien")
    public String saveTechnicien(Model model,@Valid User technicien, BindingResult bindingResult,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors())
            return "editTechnicien";
        User technicien1 = userService.findUserById(technicien.getId());
        technicien.setRoles(technicien1.getRoles());
        userService.save(technicien);
        model.addAttribute("technicien", technicien);
        return "redirect:/techniciens?page="+page+"&keyword="+keyword;
    }


    private String getUsername(){
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            return null;
        }
    }

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private List<String> getAuthenticationAuthorities(){
        return getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }


}
