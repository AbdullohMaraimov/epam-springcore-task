package gym.crm.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;


@Service
public class TimeMeasurementService {

    private final Timer traineeRegisterTimer;
    private final Timer trainerRegisterTimer;

    public TimeMeasurementService(MeterRegistry meterRegistry) {
        this.traineeRegisterTimer = meterRegistry.timer("custom.method.execution.time", "method", "registerTrainee");
        this.trainerRegisterTimer = meterRegistry.timer("custom.method.execution.time", "method", "registerTrainer");
    }

    public <T> T measureTraineeRegistrationTime(Callable<T> callable) throws Exception {
        return traineeRegisterTimer.recordCallable(callable);
    }

    public <T> T measureTrainerRegistrationTime(Callable<T> callable) throws Exception {
        return trainerRegisterTimer.recordCallable(callable);
    }
}