package com.ravsdev.ropa.controller;

import com.ravsdev.ropa.dto.PromocionDTO;
import com.ravsdev.ropa.dto.PromocionPrendasDTO;
import com.ravsdev.ropa.entity.Promocion;
import com.ravsdev.ropa.exceptions.AlreadyExistsException;
import com.ravsdev.ropa.response.ItemsResponse;
import com.ravsdev.ropa.response.ResponseMessage;
import com.ravsdev.ropa.service.PromocionService;

import static com.ravsdev.ropa.util.GeneratePromocionRef.generateRef;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/promociones")
@Validated
public class PromocionController {
    @Autowired
    PromocionService service;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Obtiene todas las promociones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muestra una lista con toda las promociones", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PromocionDTO.class))) }),
            @ApiResponse(responseCode = "204", description = "Devuelve contenido vacío.", content = @Content) })
    @GetMapping(value = { "", "/" })
    public ResponseEntity<ItemsResponse> findAll(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Promocion> promociones;
        if (nombre != null)
            promociones = service.findByNombre(nombre, page);
        else
            promociones = service.findAll(page);
        if (promociones.isEmpty())
            return ResponseEntity.noContent().build();
        List<PromocionDTO> promocionDTOS = promociones.getContent().stream()
                .map(promocion -> modelMapper.map(promocion, PromocionDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ItemsResponse(promociones, promocionDTOS));
    }

    @Operation(summary = "Obtiene una promoción dada una id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción encontrada", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PromocionDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada", content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Promocion> promocion = service.findById(id.toUpperCase());
        if (promocion.isEmpty())
            return ResponseEntity.noContent().build();
        PromocionDTO promocionDTO = modelMapper.map(promocion, PromocionDTO.class);
        return ResponseEntity.ok(promocionDTO);
    }

    @Operation(summary = "Crea una nueva promoción", description = "La id se genera automáticamente a partir del nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promoción creada correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PromocionDTO.class))
            }),
            @ApiResponse(responseCode = "409", description = "No se puede crear porque la ID de la promoción ya existe")
    })
    @PostMapping(value = { "", "/" })
    public ResponseEntity<PromocionDTO> create(@Valid @RequestBody PromocionDTO promocionDTO) {
        Promocion promocion = modelMapper.map(promocionDTO, Promocion.class);

        promocion.setId(generateRef(promocion.getNombre()));

        service.findById(promocion.getId())
                .ifPresent(p -> {
                    throw new AlreadyExistsException("Promoción con ID: " + p.getId() + " ya existe");
                });
        Promocion createPromocion = service.save(promocion);

        PromocionDTO createPromocionDTO = modelMapper.map(createPromocion, PromocionDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createPromocionDTO);
    }

    @Operation(summary = "Elimina una promoción dada una ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promoción eliminada"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@Parameter(description = "ID de la promoción") @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/prendas")
    public ResponseEntity<?> addPrendasToPromocion(@PathVariable String id,
            @Valid @RequestBody List<String> prendas) {
        service.addPrendasToPromocion(id, prendas);

        return ResponseEntity.ok(new ResponseMessage(200, "Prendas añadidas"));

    }

    @DeleteMapping("/{id}/prendas")
    public ResponseEntity<?> deletePrendasFromPromocion(@PathVariable String id,
            @RequestBody List<String> prendas) {
        service.deletePrendasFromPromocion(id, prendas);

        return ResponseEntity.ok(new ResponseMessage(200, "Prendas borradas"));

    }
}
