package com.maomaoyu.zhihu.controller;

import com.maomaoyu.zhihu.bean.User;
import com.maomaoyu.zhihu.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * maomaoyu    2018/12/14_16:43
 **/
@Controller
public class IndexController {
    private static  final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    WendaService wendaService;

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        LOGGER.error("Visit home,html");
        return wendaService.getMessage(2) + "Hello maomaoyu" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",required = false) String key){
        return String.format("Profile Page of %s / %d, t:%d k: %s",groupId,userId,type,key);
    }

    @RequestMapping(path = {"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","vvvvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED","YELLOW","GREEN"});
        model.addAttribute("colors",colors);
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < 4 ; i++) {
            map.put(String.valueOf(i),String.valueOf(i * 2));
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("LEE"));
        return "home";
    }

    /**
     *  request请求
     * */
    @RequestMapping(path = {"/requset"},method = {RequestMethod.GET})
    @ResponseBody
    public String requset(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String cookieId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE: " + cookieId + "<br>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + " : " + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookid: " + cookie.getName() + " value: " + cookie.getValue());
            }
        }
        sb.append(request.getMethod()+ "<br>");
        sb.append(request.getQueryString()+ "<br>");
        sb.append(request.getPathInfo()+ "<br>");
        sb.append(request.getRequestURL());

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public RedirectView redirectView(@PathVariable("code") int code,HttpSession httpSession){
        httpSession.setAttribute("msg" ,"jump from redirect");
        RedirectView red = new RedirectView("/",true);
        if (code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if ("maomaoyu".equals(key)){
            return "hello maomaoyu";
        }
        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }




}
