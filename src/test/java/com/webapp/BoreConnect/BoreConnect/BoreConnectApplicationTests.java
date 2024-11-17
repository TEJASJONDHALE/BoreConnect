package com.webapp.BoreConnect.BoreConnect;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.webapp.BoreConnect.BoreConnect.controller.BoreController;
import com.webapp.BoreConnect.BoreConnect.service.BoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BoreConnectApplicationTests {

	@Mock
	private BoreService boreService;

	@InjectMocks
	private BoreController boreController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(boreController).build();
	}

	@Test
	void contextLoads() {
		assertNotNull(boreController);
		assertNotNull(boreService);
	}

	@Test
	void homePageShouldReturnIndexTemplate() throws Exception {
		mockMvc.perform(get("/api/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	void startBoreShouldReturnSuccessMessage() throws Exception {
		// Arrange
		String expectedOutput = "Bore started successfully on port 8080\nConnected to: bore.pub:12345";
		when(boreService.startBoreLocal(anyInt()))
				.thenReturn(CompletableFuture.completedFuture(expectedOutput));

		// Act & Assert
		mockMvc.perform(post("/api/start")
						.param("port", "8080"))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedOutput));

		verify(boreService).startBoreLocal(8080);
	}

	@Test
	void stopBoreShouldReturnSuccessMessage() throws Exception {
		// Act & Assert
		mockMvc.perform(post("/api/stop"))
				.andExpect(status().isOk())
				.andExpect(content().string("Bore service stopped"));

		verify(boreService).stopBore();
	}

	@Test
	void getOutputShouldReturnCurrentOutput() throws Exception {
		// Arrange
		String expectedOutput = "Current bore output";
		when(boreService.getCurrentUrl()).thenReturn(expectedOutput);

		// Act & Assert
		mockMvc.perform(get("/api/output"))
				.andExpect(status().isOk())
				.andExpect(content().string(expectedOutput));

		verify(boreService).getCurrentUrl();
	}
}