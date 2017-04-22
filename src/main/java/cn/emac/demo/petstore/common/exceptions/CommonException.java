package cn.emac.demo.petstore.common.exceptions;

/**
 * 基础异常类,描述业务无关的异常
 *
 * @author Emac
 * @since 2016-03-04
 */
public class CommonException extends RuntimeException {

    /**
     * @param message
     */
    public CommonException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public CommonException(Throwable cause) {
        super(cause);
    }
}
