package gym.crm.mapper;

import gym.crm.dto.reponse.TraineeResponse;
import gym.crm.dto.request.TraineeRequest;
import gym.crm.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraineeMapperTest {

    private TraineeMapper traineeMapper;

    @BeforeEach
    void setUp() {
        traineeMapper = new TraineeMapper();
    }

    @Test
    void toTrainee() {
        TraineeRequest traineeRequest = new TraineeRequest(
                "Iman",
                "Gadzhi",
                LocalDate.of(2000, 1, 1),
                "USA",
                true
        );

        Trainee trainee = traineeMapper.toTrainee(traineeRequest);

        assertEquals("Iman", trainee.getFirstName());
        assertEquals("Gadzhi", trainee.getLastName());
        assertEquals(LocalDate.of(2000,1, 1), trainee.getDateOfBirth());
        assertEquals("USA", trainee.getAddress());
        assertEquals("Iman.Gadzhi", trainee.getUsername());
        assertEquals(true, trainee.getIsActive());

    }

    @Test
    void toTraineeResponse() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("Iman")
                .lastName("Gadzhi")
                .username("Iman.Gadzhi")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        TraineeResponse traineeResponse = traineeMapper.toTraineeResponse(trainee);

        assertEquals("Iman", traineeResponse.firstName());
        assertEquals("Gadzhi", traineeResponse.lastName());
        assertEquals("Iman.Gadzhi", traineeResponse.username());
        assertEquals(LocalDate.of(2000, 1, 1), traineeResponse.dateOfBirth());
        assertEquals("USA", traineeResponse.address());
        assertEquals(true, trainee.getIsActive());
    }

    @Test
    void toTraineeResponses() {
        Trainee trainee1 = Trainee.builder()
                .id(1L)
                .firstName("Iman")
                .lastName("Gadzhi")
                .username("Iman.Gadzhi")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        Trainee trainee2 = Trainee.builder()
                .id(2L)
                .firstName("Jim")
                .lastName("Rohn")
                .username("Jim.Rohn")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        List<Trainee> trainees = List.of(trainee1, trainee2);

        List<TraineeResponse> traineeResponses = traineeMapper.toTraineeResponses(trainees);

        assertEquals(2, traineeResponses.size());
    }

    @Test
    void toUpdatedTrainee() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("Iman")
                .lastName("Gadzhi")
                .username("Iman.Gadzhi")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("USA")
                .isActive(true)
                .build();

        TraineeRequest traineeRequest = new TraineeRequest(
                "Imanjan",
                "Gadzhi",
                LocalDate.of(2000, 1, 1),
                "UZB",
                true
        );

        Trainee updatedTrainee = traineeMapper.toUpdatedTrainee(trainee, traineeRequest);

        assertEquals("Imanjan", updatedTrainee.getFirstName());
        assertEquals("Gadzhi", updatedTrainee.getLastName());
        assertEquals("UZB", updatedTrainee.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), updatedTrainee.getDateOfBirth());
        assertEquals(true, updatedTrainee.getIsActive());
    }
}