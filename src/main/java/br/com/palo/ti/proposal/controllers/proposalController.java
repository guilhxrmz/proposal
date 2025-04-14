package br.com.palo.ti.proposal.controllers;

import br.com.palo.ti.proposal.dtos.proposalDto;
import br.com.palo.ti.proposal.services.proposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/proposals")
@Tag(name = "Proposals", description = "Endpoints for managing job proposals")
public class proposalController {

    @Autowired
    private proposalService service;

    @GetMapping
    @Operation(summary = "Get all proposals", description = "Retrieve a list of all job proposals")
    public ResponseEntity<List<proposalDto>> getAllProposals() {
        return ResponseEntity.ok(service.getAllProposals());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a proposal by ID", description = "Retrieve a specific job proposal by its unique ID")
    public ResponseEntity<proposalDto> getProposalById(
            @Parameter(description = "ID of the proposal to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getProposalById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new proposal", description = "Create a new job proposal with the provided details")
    public ResponseEntity<proposalDto> createProposal(
            @Parameter(description = "Details of the proposal to create", required = true)
            @RequestBody @Valid proposalDto dto) {
        return ResponseEntity.ok(service.createProposal(dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing proposal", description = "Update the details of an existing job proposal")
    public ResponseEntity<proposalDto> updateProposal(
            @Parameter(description = "ID of the proposal to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated details of the proposal", required = true)
            @RequestBody proposalDto dto) {
        return ResponseEntity.ok(service.updateProposal(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a proposal", description = "Delete a specific job proposal by its unique ID")
    public ResponseEntity<Void> deleteProposal(
            @Parameter(description = "ID of the proposal to delete", required = true)
            @PathVariable UUID id) {
        service.deleteProposal(id);
        return ResponseEntity.noContent().build();
    }
}