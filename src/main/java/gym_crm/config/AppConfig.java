package gym_crm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gym_crm.mapper.TraineeMapper;
import gym_crm.mapper.TrainerMapper;
import gym_crm.mapper.TrainingMapper;
import gym_crm.repository.TraineeDAO;
import gym_crm.repository.TrainerDAO;
import gym_crm.repository.TrainingDAO;
import gym_crm.service.TraineeService;
import gym_crm.service.TrainerService;
import gym_crm.service.TrainingService;
import gym_crm.service.impl.TraineeServiceImpl;
import gym_crm.service.impl.TrainerServiceImpl;
import gym_crm.service.impl.TrainingServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "gym_crm")
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public TraineeDAO traineeDAO() {
        return new TraineeDAO();
    }

    @Bean
    public TrainerDAO trainerDAO() {
        return new TrainerDAO();
    }

    @Bean
    public TrainingDAO trainingDAO() {
        return new TrainingDAO();
    }

    @Bean
    public TraineeMapper traineeMapper() {
        return new TraineeMapper();
    }

    @Bean
    public TrainerMapper trainerMapper() {
        return new TrainerMapper();
    }

    @Bean
    public TrainingMapper trainingMapper() {
        return new TrainingMapper();
    }

    @Bean
    public TraineeService traineeService() {
        return new TraineeServiceImpl(traineeMapper(), traineeDAO());
    }

    @Bean
    public TrainerService trainerService() {
        return new TrainerServiceImpl(trainerMapper(), trainerDAO());
    }

    @Bean
    public TrainingService trainingService() {
        return new TrainingServiceImpl(trainingDAO(), trainerDAO(), traineeDAO(), trainingMapper());
    }

}
