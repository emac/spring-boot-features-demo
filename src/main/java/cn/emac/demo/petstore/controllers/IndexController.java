package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.pagination.LinkedPage;
import cn.emac.demo.petstore.common.pagination.PageBuilder;
import cn.emac.demo.petstore.components.AsyncExecutor;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.services.RetryService;
import cn.emac.demo.petstore.services.SignonService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Emac
 * @since 2016-02-10
 */
@Controller
@Validated
public class IndexController implements IController {

    @Autowired
    private AsyncExecutor asyncExecutor;

    @Autowired
    private SignonService signonService;

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
        asyncExecutor.delay(1000, "async").addCallback(new ListenableFutureCallback<String>() {
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
        ListenableFuture<String> result = asyncExecutor.delay(1000, "async2");
        return result;
    }

    @RequestMapping(value = "/async3", method = RequestMethod.GET)
    @ResponseBody
    @Async
    public ListenableFuture<String> async3() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        return AsyncResult.forValue("async3");
    }

    @RequestMapping(value = "/async4", method = RequestMethod.GET)
    @ResponseBody
    public ListenableFuture<String> async4() {
        return asyncExecutor.invoke(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
            return "async4";
        });
    }

    @RequestMapping(value = "/retry", method = RequestMethod.GET)
    @ResponseBody
    public String retry() {
        return retryService.retry();
    }

    @RequestMapping(value = "/nextDay", method = RequestMethod.GET)
    @ResponseBody
    public Day nextDay(@Valid Day day) {
        return day.next();
    }

    @Getter
    @AllArgsConstructor
    private enum WeekDay {
        SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6);
        private Integer value;

        public static WeekDay from(DayOfWeek dayOfWeek) {
            if (DayOfWeek.SUNDAY.equals(dayOfWeek)) {
                return SUNDAY;
            }
            return of(dayOfWeek.getValue());
        }

        public static WeekDay of(Integer value) {
            return Arrays.stream(WeekDay.values())
                    .filter(d -> d.getValue().equals(value)).findFirst().orElse(null);
        }

        public WeekDay next() {
            Integer next = (value + 1) % 7;
            return of(next);
        }
    }

    @Data
    private static class Day {
        @NotNull
        private WeekDay day;
        private OffsetDateTime time;

        public void setTime(OffsetDateTime time) {
            this.time = time;
            this.day = WeekDay.from(time.getDayOfWeek());
        }

        public Day next() {
            Day nextDay = new Day();
            nextDay.setTime(time.plusDays(1));
            return nextDay;
        }
    }
}
