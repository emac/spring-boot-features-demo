package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.LinkedPage;
import cn.emac.demo.petstore.common.PageBuilder;
import cn.emac.demo.petstore.components.AsyncExecutor;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.services.RetryService;
import cn.emac.demo.petstore.services.SignonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

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

    @Autowired
    private AsyncExecutor asyncExecutor;

    @Autowired
    private RetryService retryService;

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

    @RequestMapping(value = "/async", method = RequestMethod.GET)
    @ResponseBody
    public DeferredResult<String> async() {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        _async(1000).addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable ex) {
                deferredResult.setErrorResult(ex);
            }

            @Override
            public void onSuccess(String result) {
                deferredResult.setResult(result);
            }
        });
        return deferredResult;
    }

    @RequestMapping(value = "/async2", method = RequestMethod.GET)
    @ResponseBody
    public ListenableFuture<String> async2() {
        return _async(2000);
    }

    @RequestMapping(value = "/async3", method = RequestMethod.GET)
    @ResponseBody
    @Async
    public ListenableFuture<String> async3() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // ignore
        }
        return AsyncResult.forValue("sync3");
    }

    @RequestMapping(value = "/async4", method = RequestMethod.GET)
    @ResponseBody
    public ListenableFuture<String> async4() {
        return asyncExecutor.invoke(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                // ignore
            }
            return "async";
        });
    }

    @Async
    public ListenableFuture<String> _async(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // ignore
        }
        return AsyncResult.forValue("async");
    }

    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    @ResponseBody
    public String retry() {
        return retryService.retry();
    }
}
