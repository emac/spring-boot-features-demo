package cn.emac.demo.petstore.common.exceptions;

/**
 * Client异常类
 *
 * @author Emac
 * @since 2016-09-27
 */
public class ClientException extends CommonException {

    /**
     * @param message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public ClientException(Throwable cause) {
        super(cause);
    }
}
