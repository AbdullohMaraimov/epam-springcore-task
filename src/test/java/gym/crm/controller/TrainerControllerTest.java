//package gym.crm.controller;
//
//import gym.crm.dto.reponse.ApiResponse;
//import gym.crm.dto.reponse.TrainerResponse;
//import gym.crm.dto.request.TrainerRequest;
//import gym.crm.service.TrainerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TrainerControllerTest {
//
//    @Mock
//    private TrainerService trainerService;
//
//    @InjectMocks
//    private TrainerController trainerController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void create() {
//        TrainerRequest request = new TrainerRequest("John", "Doe", "GYM");
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Trainer created", true);
//
//        when(trainerService.create(any(TrainerRequest.class))).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = trainerController.create(request);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("Trainer created", response.message());
//        verify(trainerService, times(1)).create(any(TrainerRequest.class));
//        verifyNoMoreInteractions(trainerService);
//    }
//
//    @Test
//    void findByUsername() {
//        String username = "Iman.Gadzhi";
//        TrainerResponse trainerResponse = new TrainerResponse("id", "Iman", "Gadzhi", "Iman.Gadzhi", "GYM", true);
//        ApiResponse<TrainerResponse> apiResponse = new ApiResponse<>(200, trainerResponse, "Trainer found", true);
//
//        when(trainerService.findByUsername(username)).thenReturn(apiResponse);
//
//        ApiResponse<TrainerResponse> response = trainerController.findByUsername(username);
//
//        assertEquals(200, response.statusCode());
//        assertEquals("Trainer found", response.message());
//        assertEquals(trainerResponse, response.data());
//
//        verify(trainerService, times(1)).findByUsername(username);
//        verifyNoMoreInteractions(trainerService);
//    }
//
//    @Test
//    void findAll() {
//        List<TrainerResponse> trainers = List.of(
//                new TrainerResponse("id1", "John", "Doe", "John.Doe", "Fitness", true),
//                new TrainerResponse("id2", "Jim", "Rohn", "Jim.Rohn", "Yoga", true)
//        );
//
//        ApiResponse<List<TrainerResponse>> apiResponse = new ApiResponse<>(200, trainers, "Trainers found", true);
//
//        when(trainerService.findAll()).thenReturn(apiResponse);
//
//        ApiResponse<List<TrainerResponse>> response = trainerController.findAll();
//
//        assertEquals(200, response.statusCode());
//        assertEquals("Trainers found", response.message());
//        assertEquals(trainers, response.data());
//
//        verify(trainerService, times(1)).findAll();
//        verifyNoMoreInteractions(trainerService);
//    }
//
//    @Test
//    void update() {
//        String username = "Jim.Rohn";
//        TrainerRequest request = new TrainerRequest("Jim", "Rohn", "Fitness");
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Trainer updated", true);
//
//        when(trainerService.update(eq(username), any(TrainerRequest.class))).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = trainerController.update(username, request);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("Trainer updated", response.message());
//
//        verify(trainerService, times(1)).update(eq(username), any(TrainerRequest.class));
//        verifyNoMoreInteractions(trainerService);
//    }
//}