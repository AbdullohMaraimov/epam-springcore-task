//package gym.crm.controller;
//
//import gym.crm.dto.reponse.ApiResponse;
//import gym.crm.dto.reponse.TraineeResponse;
//import gym.crm.dto.reponse.TrainerResponse;
//import gym.crm.dto.request.TraineeRequest;
//import gym.crm.service.TraineeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TraineeControllerTest {
//
//    @Mock
//    private TraineeService traineeService;
//
//    @InjectMocks
//    private TraineeController traineeController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void update() {
//        String username = "Iman.Gadzhi";
//        TraineeRequest request = new TraineeRequest("Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true);
//        ApiResponse<TraineeResponse> apiResponse = new ApiResponse<>(204, "Trainee updated", true);
//
//        when(traineeService.update(eq(username), any(TraineeRequest.class))).thenReturn(apiResponse);
//
//        ApiResponse<TraineeResponse> response = traineeController.update(username, request);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("Trainee updated", response.message());
//
//        verify(traineeService, times(1)).update(eq(username), any(TraineeRequest.class));
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void findByUsername() {
//        String username = "Iman.Gadzhi";
//        TraineeResponse traineeResponse = new TraineeResponse(1L, "Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true, null);
//        ApiResponse<TraineeResponse> apiResponse = new ApiResponse<>(200, traineeResponse, "Trainee found", true);
//
//        when(traineeService.findByUsername(username)).thenReturn(apiResponse);
//
//        ApiResponse<TraineeResponse> response = traineeController.findByUsername(username);
//
//        assertEquals(200, response.statusCode());
//        assertEquals("Trainee found", response.message());
//        assertEquals(traineeResponse, response.data());
//        verify(traineeService, times(1)).findByUsername(username);
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void findAll() {
//        List<TraineeResponse> trainees = List.of(
//                new TraineeResponse(1L, "Iman", "Gadzhi", LocalDate.of(2000, 1, 1), "USA", true, null),
//                new TraineeResponse(1L, "Ali", "Vali", LocalDate.of(2002, 2, 2), "USA", true, null)
//        );
//
//        ApiResponse<List<TraineeResponse>> apiResponse = new ApiResponse<>(200, trainees, "Trainees found", true);
//
//        when(traineeService.findAll()).thenReturn(apiResponse);
//
//        ApiResponse<List<TraineeResponse>> response = traineeController.findAll();
//
//        assertEquals(200, response.statusCode());
//        assertEquals("Trainees found", response.message());
//        assertEquals(trainees, response.data());
//        verify(traineeService, times(1)).findAll();
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void deleteByUsername() {
//        String username = "Iman.Gadzhi";
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Deleted successfully!", true);
//
//        when(traineeService.delete(username)).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = traineeController.deleteByUsername(username);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("Deleted successfully!", response.message());
//        verify(traineeService, times(1)).delete(username);
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void deleteAll() {
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "All trainees deleted", true);
//
//        when(traineeService.deleteAll()).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = traineeController.deleteAll();
//
//        assertEquals(204, response.statusCode());
//        assertEquals("All trainees deleted", response.message());
//        verify(traineeService, times(1)).deleteAll();
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void deActivateUser() {
//        String username = "Iman.Gadzhi";
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "User deactivated", true);
//
//        when(traineeService.deActivateUser(username)).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = traineeController.deActivateUser(username);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("User deactivated", response.message());
//        verify(traineeService, times(1)).deActivateUser(username);
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void activateUser() {
//        String username = "Iman.Gadzhi";
//        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "User activated", true);
//
//        when(traineeService.activateUser(username)).thenReturn(apiResponse);
//
//        ApiResponse<Void> response = traineeController.activateUser(username);
//
//        assertEquals(204, response.statusCode());
//        assertEquals("User activated", response.message());
//        verify(traineeService, times(1)).activateUser(username);
//        verifyNoMoreInteractions(traineeService);
//    }
//
//    @Test
//    void getAllUnassignedTrainers() {
//        String username = "Iman.Gadzhi";
//        List<TrainerResponse> trainers = List.of(
//                new TrainerResponse(1L, "John", "Doe", "GYM", true, null),
//                new TrainerResponse(2L, "Jane", "Doe", "GYM", true, null)
//        );
//        ApiResponse<List<TrainerResponse>> apiResponse = new ApiResponse<>(200, trainers, "Unassigned trainers found", true);
//
//        when(traineeService.findAllUnassignedTrainers(username)).thenReturn(apiResponse);
//
//        ApiResponse<List<TrainerResponse>> response = traineeController.getAllUnassignedTrainers(username);
//
//        assertEquals(200, response.statusCode());
//        assertEquals("Unassigned trainers found", response.message());
//        assertEquals(trainers, response.data());
//        verify(traineeService, times(1)).findAllUnassignedTrainers(username);
//        verifyNoMoreInteractions(traineeService);
//    }
//
//}