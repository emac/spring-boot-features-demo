package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 提供统一测活接口
 *
 * @author Emac
 * @since 2016-10-18
 */
@Controller
public class AliveController implements IController {

    @RequestMapping("/alive")
    @ResponseBody
    public JsonResult alive() {
        return JsonResult.ok();
    }
}
