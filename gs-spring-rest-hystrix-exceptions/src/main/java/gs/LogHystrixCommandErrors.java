package gs;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.contrib.javanica.command.AbstractHystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.CommandAction;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * <p>HystrixCommandExecutionHook used to log errors.</p>
 *
 * <p>The errors will be logged only if the command fails. Meaning that, if the command has a fallback which runs successfully, nothing will be logged.</p>
 *
 * <p>Examples of generated logs:</p>
 *
 * <pre>
07:24:39.105 WARN [...] gs.LogHystrixCommandErrors: Hystrix command RemoteServiceAdapter.callRemote[1234] failed due to TIMEOUT

07:24:40.208 WARN [...] gs.LogHystrixCommandErrors: Hystrix command RemoteServiceAdapter.callRemote[1234] failed due to COMMAND_EXCEPTION
org.springframework.web.client.HttpServerErrorException: 500 Internal Server Error
  at org.springframework.web.client.DefaultResponseErrorHandler.handleError(DefaultResponseErrorHandler.java:94) ~[spring-web-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  [...]
 * </pre>
 */
public class LogHystrixCommandErrors extends HystrixCommandExecutionHook {

    private static Logger LOGGER = LoggerFactory.getLogger(LogHystrixCommandErrors.class);

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
        AbstractHystrixCommand command = (AbstractHystrixCommand) commandInstance;
        switch (failureType) {
            case TIMEOUT:
            case SHORTCIRCUIT:
            case REJECTED_THREAD_EXECUTION:
            case REJECTED_SEMAPHORE_EXECUTION:
            case REJECTED_SEMAPHORE_FALLBACK:
                // don't log stacktrace
                LOGGER.warn("Hystrix command {}.{}{} failed due to {}", command.getCommandGroup(), command.getCommandKey(), getArguments(command), failureType);
                break;
            default:
                // log stacktrace
                LOGGER.warn("Hystrix command {}.{}{} failed due to {}", command.getCommandGroup(), command.getCommandKey(), getArguments(command), failureType, e);
        }
        return e;
    }

    private Object[] getArguments(AbstractHystrixCommand command) {
        Object[] args;
        Method getCommandActionMethod = null;
        boolean initialAccessibility = false;
        try {
            getCommandActionMethod = AbstractHystrixCommand.class.getDeclaredMethod("getCommandAction");
            initialAccessibility = getCommandActionMethod.isAccessible();
            getCommandActionMethod.setAccessible(true);
            args = ((CommandAction) getCommandActionMethod.invoke(command)).getMetaHolder().getArgs();
        } catch(Exception exception) {
            LOGGER.warn("Failed to find hystrix command arguments by reflection: {}", exception.getMessage());
            args = new Object[0];
        } finally {
            if (getCommandActionMethod != null) {
                getCommandActionMethod.setAccessible(initialAccessibility);
            }
        }
        return args;
    }
}
