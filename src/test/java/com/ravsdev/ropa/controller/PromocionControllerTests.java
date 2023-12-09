package com.ravsdev.ropa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PromocionControllerTests {
    private static final String PRENDAS_END_POINT_PATH = "/v1/prendas";
    private static final String END_POINT_PATH = "/v1/promociones";

    @Autowired
    private MockMvc mockMvc;

    private final String promocion = "Black Friday 2023";
    private final String promocionID = "BF2023";

    private final String prendaId = "S000000001";

    @BeforeEach
    public void init() throws Exception {
        BigDecimal descuento = new BigDecimal("50.00");

        mockMvc.perform(post(END_POINT_PATH)
                .content("{\"nombre\": \"" + this.promocion + "\", \"descuento\": \"" + descuento + "\"}")
                .contentType("application/json"));

        BigDecimal prendaPrecio = new BigDecimal("7.95");

        mockMvc.perform(post(PRENDAS_END_POINT_PATH)
                .content("{\"id\": \"" + this.prendaId + "\", \"precio\": \"" + prendaPrecio
                        + "\"}")
                .contentType("application/json"));
    }

    @Test
    @DisplayName("Obtiene el listado de promociones")
    public void getAllPromociones() throws Exception {

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    @DisplayName("Añadir prendas a una promoción")
    public void addPrendasPromocion() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.promocionID + "/prendas";

        mockMvc.perform(put(requestURI)
                .content("[\"" + this.prendaId + "\"]")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Prendas añadidas"))
                .andDo(print());
    }

    @Test
    @DisplayName("Eliminar prendas de una promoción")
    public void removePrendasPromocion() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.promocionID + "/prendas";

        mockMvc.perform(delete(requestURI)
                .content("[\"" + this.prendaId + "\"]")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Prendas borradas"))
                .andDo(print());
    }
    @Test
    @DisplayName("Falla al añadir prendas que no existen a una promoción")
    public void addPrendasFailsPromocion() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.promocionID + "/prendas";
        String wrongPrendaID = "WRONGID";
        mockMvc.perform(put(requestURI)
                        .content("[\""+wrongPrendaID+"\"]")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Prenda "+wrongPrendaID+" no encontrada"))
                .andDo(print());
    }
}