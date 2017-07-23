package gs.hystrixcontext;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>HystrixCommandExecutionHook used on hystrix child threads to
 * fill the Mapped Diagnostic Context with a correlation ID.</p>
 */
public class AddCorrelationIdToDiagnosticContext extends HystrixCommandExecutionHook {

    Logger LOGGER = LoggerFactory.getLogger(AddCorrelationIdToDiagnosticContext.class);

    static {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCommandExecutionHook() {
            @Override
            public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
                return super.onError(commandInstance, failureType, e);
            }
        });
    }

    @Override
    public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {



    }

    @Override
    public <T> void onThreadComplete(HystrixInvokable<T> commandInstance) {

    }
}
