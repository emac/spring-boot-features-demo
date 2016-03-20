package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.LinkedPage;
import com.google.common.collect.Lists;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Emac
 * @since 2016-02-10
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("recipient", "World");
        return "index";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @PreAuthorize("authenticated and hasPermission('clinic', 'manager')")
    public String hello(HttpSession session, Model model) {
        Object sessionVar = session.getAttribute("var");
        if (sessionVar == null) {
            sessionVar = new Date();
            session.setAttribute("var", sessionVar);
        }
        model.addAttribute("message", "Message").addAttribute("sessionVar", sessionVar);
        return "hello";
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String page(Model model, Pageable pageable) {
        List<String> content = Lists.newArrayList();
        IntStream.range(0, 20).forEach(i -> content.add(String.valueOf(i)));
        // Pageable起始计数为0
        PageImpl p = new PageImpl(content, pageable, 120);
        LinkedPage<String> page = new LinkedPage<>(p, "/page");
        model.addAttribute("page", page);
        return "page";
    }
}
