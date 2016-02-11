package cn.emac.demo.petstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Emac
 * @since 2016-02-11
 */
@Controller
public class SecurityController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("/accessDenied")
    @ResponseBody
    public String accessDenied() {
        return "你没有权限访问该页面";
    }

    @RequestMapping("/expired")
    @ResponseBody
    public String expired() {
        return "会话过期，请重新登录";
    }
}
