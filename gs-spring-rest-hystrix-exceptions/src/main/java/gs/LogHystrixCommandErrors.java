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
 * Examples of generated logs:
 * <pre>
 * 15:38:11.277 WARN [...] g.h.LogHystrixCommandErrors: Hystrix command RemoteServiceAdapter.callRemote[0000] failed due to COMMAND_EXCEPTION
 *   [...stacktrace...]
 * 15:38:09.751 WARN [...] g.h.LogHystrixCommandErrors: Hystrix command RemoteServiceAdapter.callRemote[1234] failed due to TIMEOUT
 *   [...stacktrace...]
 * </pre>
 */
public class LogHystrixCommandErrors extends HystrixCommandExecutionHook {

    private static Logger LOGGER = LoggerFactory.getLogger(LogHystrixCommandErrors.class);

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
        AbstractHystrixCommand command = (AbstractHystrixCommand) commandInstance;
        LOGGER.warn("Hystrix command {}.{}{} failed due to {}", command.getCommandGroup(), command.getCommandKey(), getArguments(command), failureType, e);
        return super.onError(commandInstance, failureType, e);
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
