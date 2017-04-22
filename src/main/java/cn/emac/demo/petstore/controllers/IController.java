package cn.emac.demo.petstore.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 通用Controller接口
 *
 * @author Emac
 * @since 2016-10-08
 */
public interface IController {

    /**
     * 返回与当前线程绑定的request对象
     *
     * @return
     */
    default HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 返回与当前线程绑定的response对象
     *
     * @return
     */
    default HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 返回与当前线程绑定的session对象
     *
     * @return
     */
    default HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 返回应用的根URL, e.g. http://localhost:3000/&lt;contextPath&gt;
     */
    default String getRootUrl() {
        HttpServletRequest request = getRequest();
        return request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf(request.getServletPath()));
    }

    /**
     * 返回当前request的客户端IP
     */
    default String getClientIP() {
        String clientIP = getRequest().getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(clientIP)) {
            clientIP = getRequest().getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(clientIP)) {
            clientIP = getRequest().getRemoteAddr();
        }
        return clientIP;
    }
}
