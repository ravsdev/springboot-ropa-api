package com.ravsdev.ropa.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ravsdev.ropa.dto.PrendaDTO;
import com.ravsdev.ropa.dto.PrendaPrecioPromocionDTO;
import com.ravsdev.ropa.exceptions.NotFoundException;
import com.ravsdev.ropa.response.ItemsResponse;
import com.ravsdev.ropa.exceptions.AlreadyExistsException;
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

import com.ravsdev.ropa.entity.Prenda;
import com.ravsdev.ropa.service.PrendaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/prendas")
@Validated
public class PrendaController {

        @Autowired
        private PrendaService service;
        @Autowired
        private ModelMapper modelMapper;

        @Operation(summary = "Obtiene todas las prendas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Muestra una lista con toda las prendas", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PrendaDTO.class))) }),
                        @ApiResponse(responseCode = "204", description = "Devuelve contenido vacío.", content = @Content) })
        @GetMapping(value = { "", "/" })
        public ResponseEntity<?> findAll(
                        @RequestParam(name = "promocion", required = false) String promocion,
                        @Parameter(description = "Número de página") @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
                        @Parameter(description = "Número de prendas a mostrar por página") @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
                Pageable page = PageRequest.of(pageNumber, pageSize);
                Page<Prenda> prendas;

                if (promocion != null)
                        prendas= service.findByPromo(promocion,page);
                else prendas = service.findAll(page);

                if (prendas.isEmpty())
                        return ResponseEntity.noContent().build();

                List<PrendaDTO> prendaDTOS = prendas.getContent().stream()
                                .map(prenda -> modelMapper.map(prenda, PrendaDTO.class))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(new ItemsResponse(prendas, prendaDTOS));
        }

        @Operation(summary = "Obtiene una prenda dada una id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Prenda encontrada", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PrendaDTO.class)) }),
                        @ApiResponse(responseCode = "404", description = "Prenda no encontrada", content = @Content)
        })
        @GetMapping("{id}")
        public ResponseEntity<?> findById(@Parameter(description = "ID de la prenda") @PathVariable("id") String id,
                        @Parameter(description = "Precio promocionado") @RequestParam(required = false, defaultValue = "false") Boolean promo) {
                Optional<Prenda> prenda = service.findById(id);
                if (!prenda.isPresent())
                        return ResponseEntity.noContent().build();
                if (promo)
                        return ResponseEntity.ok(modelMapper.map(prenda, PrendaPrecioPromocionDTO.class));
                PrendaDTO prendaDTO = modelMapper.map(prenda, PrendaDTO.class);
                return ResponseEntity.ok(prendaDTO);
        }

        @Operation(summary = "Crea una nueva prenda")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Prenda creada correctamente", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PrendaDTO.class))
                        }),
                        @ApiResponse(responseCode = "409", description = "No se puede crear porque la ID de la prenda ya existe")
        })
        @PostMapping(value = { "", "/" })
        public ResponseEntity<PrendaDTO> create(@Valid @RequestBody Prenda prendaDTO) {
                Prenda prenda = modelMapper.map(prendaDTO, Prenda.class);
                service.findById(prenda.getId())
                                .ifPresent(p -> {
                                        throw new AlreadyExistsException("Prenda con ID: " + p.getId() + " ya existe");
                                });
                Prenda createPrenda = service.save(prenda);

                PrendaDTO createPrendaDTO = modelMapper.map(createPrenda, PrendaDTO.class);
                return ResponseEntity.status(HttpStatus.CREATED).body(createPrendaDTO);
        }

        @Operation(summary = "Actualiza la información de una prenda")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Prenda encontrada", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = PrendaDTO.class)) }),
                        @ApiResponse(responseCode = "404", description = "Prenda no encontrada", content = @Content)
        })
        @PutMapping("{id}")
        public ResponseEntity<PrendaDTO> update(@PathVariable() String id, @Valid @RequestBody PrendaDTO prendaDTO) {
                service.findById(id)
                                .orElseThrow(() -> new NotFoundException("Prenda con ID " + id + " no encontrada."));
                if (prendaDTO.getId() == null)
                        prendaDTO.setId(id);
                Prenda updatePrenda = service.update(modelMapper.map(prendaDTO, Prenda.class));
                PrendaDTO updatePrendaDTO = modelMapper.map(updatePrenda, PrendaDTO.class);
                return ResponseEntity.ok(updatePrendaDTO);
        }

        @Operation(summary = "Elimina una prenda dada una ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Prenda eliminada"),
                        @ApiResponse(responseCode = "404", description = "Prenda no encontrada")
        })
        @DeleteMapping("{id}")
        public ResponseEntity<?> delete(@PathVariable String id) {
                service.delete(id);
                return ResponseEntity.noContent().build();
        }
}
