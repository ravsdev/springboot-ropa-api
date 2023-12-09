package com.ravsdev.ropa;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ravsdev.ropa.controller.PrendaController;

@SpringBootTest
class RopaApplicationTests {

	@Autowired
	PrendaController prendaController;

	@Test
	void contextLoads() throws Exception {

		assertThat(prendaController).isNotNull();
	}

}
