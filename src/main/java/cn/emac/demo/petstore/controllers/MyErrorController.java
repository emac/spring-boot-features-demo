package cn.emac.demo.petstore.controllers;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;

/**
 * @author Emac
 * @since 2016-03-20
 */
public class MyErrorController extends BasicErrorController {

    /**
     * @param errorAttributes
     * @param errorProperties
     */
    public MyErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }
}
