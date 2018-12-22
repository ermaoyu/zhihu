package com.maomaoyu.zhihu.controller;

import com.maomaoyu.zhihu.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * maomaoyu    2018/12/19_14:45
 **/
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reglogin"},method = {RequestMethod.GET})
    public String regloginPage(Model model,@RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String register(Model model,@RequestParam("username") String username,
                           @RequestParam("password")String password,
                           @RequestParam("email")String email,
                           @RequestParam(value = "next",required = false) String next,
                           @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                           HttpServletResponse response){
        try {
            Map<String, Object> map = userService.register(username, password,email);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            model.addAttribute("msg", "服务器错误");
            return "login";
        }
    }

    @RequestMapping(value = "/login/",method = {RequestMethod.POST})
    public String login(Model model,@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam("rememberme") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next; //边界条件判断
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }

    @RequestMapping(path = {"/logout"} ,method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/" ;
    }
}
