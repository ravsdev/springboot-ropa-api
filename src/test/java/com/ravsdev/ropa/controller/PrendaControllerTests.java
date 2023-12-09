package com.ravsdev.ropa.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravsdev.ropa.entity.Categorias;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PrendaControllerTests {
    private static final String END_POINT_PATH = "/v1/prendas";

    @Autowired
    private MockMvc mockMvc;

    private final String prendaId = "S000000001";

    @BeforeEach
    public void init() throws Exception {
        BigDecimal prendaPrecio = new BigDecimal("7.95");

        mockMvc.perform(post(END_POINT_PATH)
                .content("{\"id\": \"" + this.prendaId + "\", \"precio\": \"" + prendaPrecio + "\"}")
                .contentType("application/json"));
    }

    @Test
    @DisplayName("Obtiene el listado de prendas")
    public void getAllPrendas() throws Exception {

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    @DisplayName("Obtiene una prenda dada una ID")
    public void getPrendaByID() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.prendaId;

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("Obtiene una prenda con precio promocinado, dada una ID")
    public void getPrendaPromoByID() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.prendaId + "?promo=true";

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.precioPromocion").isNumber())
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("Crea una prenda nueva")
    public void createNewPrenda() throws Exception {
        String prendaId = "L000000001";
        BigDecimal prendaPrecio = new BigDecimal("15.15");

        mockMvc.perform(post(END_POINT_PATH)
                .content("{\"id\": \"" + prendaId + "\", \"precio\": \"" + prendaPrecio + "\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(prendaId))
                .andExpect(jsonPath("$.precio").value(prendaPrecio))
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("Actualiza una prenda")
    public void updatePrenda() throws Exception {
        String requestURI = END_POINT_PATH + "/" + this.prendaId;
        List<Categorias> categorias = new ArrayList<>();
        BigDecimal prendaPrecio = new BigDecimal("29.99");
        categorias.add(Categorias.MUJER);

        Map<String, Object> data = new HashMap<>();
        data.put("id", prendaId);
        data.put("precio", prendaPrecio);
        data.put("categorias", categorias);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);

        mockMvc.perform(put(requestURI)
                .content(json)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(prendaId))
                .andExpect(jsonPath("$.precio").value(prendaPrecio))
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("Falla al crear una prenda nueva con precio inferior a 0")
    public void createNewFailsPrecioPrenda() throws Exception {
        String prendaId = "M100000001";
        BigDecimal prendaPrecio = new BigDecimal("-15.15");

        mockMvc.perform(post(END_POINT_PATH)
                .content("{\"id\": \"" + prendaId + "\", \"precio\": \"" + prendaPrecio + "\"}")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andDo(print());
        ;
    }

    @Test
    @DisplayName("Falla al crear una prenda nueva con ID inválida")
    public void createNewFailsIdPrenda() throws Exception {
        String prendaId = "AB21123";
        BigDecimal prendaPrecio = new BigDecimal("10.15");

        mockMvc.perform(post(END_POINT_PATH)
                .content("{\"id\": \"" + prendaId + "\", \"precio\": \"" + prendaPrecio + "\"}")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andDo(print());
        ;
    }
}
