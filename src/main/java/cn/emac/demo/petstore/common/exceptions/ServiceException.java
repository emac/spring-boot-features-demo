package cn.emac.demo.petstore.common.exceptions;

/**
 * 服务异常类,描述业务相关的异常
 *
 * @author Emac
 * @since 2016-03-04
 */
public class ServiceException extends CommonException {

    /**
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
