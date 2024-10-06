package gym.crm.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class ApiCallService {

    private final Counter loginCounter;

    public ApiCallService(MeterRegistry meterRegistry) {
        this.loginCounter = Counter.builder("custom.api.logins")
                .description("Number of login attempts")
                .tags("endpoint", "/login")
                .register(meterRegistry);
    }

    public void trackLogin() {
        loginCounter.increment();
    }
}
