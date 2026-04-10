package com.medilabo.front;

import com.medilabo.front.config.JwtAuthenticationSuccessHandler;
import com.medilabo.front.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FrontApplicationTests {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

	@Test
	void contextLoads() {
	}

}
