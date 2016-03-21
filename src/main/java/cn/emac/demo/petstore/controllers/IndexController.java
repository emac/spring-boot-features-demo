package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.LinkedPage;
import cn.emac.demo.petstore.common.PageBuilder;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.services.SignonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Emac
 * @since 2016-02-10
 */
@Controller
public class IndexController {

    @Autowired
    private SignonService signonService;

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
    public String page(Model model, @PageableDefault(1) Pageable pageable) {
        PageBuilder<Signon> pageBuilder = signonService.findAllByPage(pageable);
        LinkedPage<Signon> page = new LinkedPage<>(pageBuilder.build(), "/page");
        model.addAttribute("page", page);
        return "page";
    }
}
