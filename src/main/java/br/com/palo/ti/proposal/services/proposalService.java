package br.com.palo.ti.proposal.services;

import br.com.palo.ti.proposal.dtos.proposalDto;
import br.com.palo.ti.proposal.dtos.statusDto;
import br.com.palo.ti.proposal.models.proposalModel;
import br.com.palo.ti.proposal.producers.candidateProducer;
import br.com.palo.ti.proposal.repositories.proposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing job proposals.
 */
@Service
public class proposalService {

    @Autowired
    private proposalRepository repository;

    @Autowired
    private candidateProducer producer;

    /**
     * Retrieve all job proposals.
     *
     * @return List of all proposals as DTOs.
     */
    public List<proposalDto> getAllProposals() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a specific job proposal by its ID.
     *
     * @param id The unique ID of the proposal.
     * @return The proposal details as a DTO.
     * @throws RuntimeException if the proposal is not found.
     */
    public proposalDto getProposalById(UUID id) {
        proposalModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found"));
        return convertToDto(model);
    }

    /**
     * Create a new job proposal.
     *
     * @param dto The proposal details as a DTO.
     * @return The created proposal as a DTO.
     */
    public proposalDto createProposal(proposalDto dto) {
        proposalModel model = convertToModel(dto);
        proposalModel savedModel = repository.save(model);
        producer.publishStatus(new statusDto(savedModel.getEmail(), savedModel.getStatus()));
        return convertToDto(savedModel);
    }

    /**
     * Update an existing job proposal.
     *
     * @param id  The unique ID of the proposal.
     * @param dto The updated proposal details as a DTO.
     * @return The updated proposal as a DTO.
     * @throws RuntimeException if the proposal is not found.
     */
    public proposalDto updateProposal(UUID id, proposalDto dto) {
        proposalModel existingModel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found"));

        if (dto.title() != null) {
            existingModel.setTitle(dto.title());
        }
        if (dto.description() != null) {
            existingModel.setDescription(dto.description());
        }
        if (dto.salary() != null) {
            existingModel.setSalary(dto.salary());
        }
        if (dto.status() != null) {
            existingModel.setStatus(dto.status());
        }
        if (dto.email() != null) {
            existingModel.setEmail(dto.email());
        }

        proposalModel updatedModel = repository.save(existingModel);
        producer.publishStatus(new statusDto(updatedModel.getEmail(), updatedModel.getStatus()));
        return convertToDto(updatedModel);
    }

    /**
     * Delete a job proposal by its ID.
     *
     * @param id The unique ID of the proposal.
     * @throws RuntimeException if the proposal is not found.
     */
    public void deleteProposal(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Proposal not found");
        }
        repository.deleteById(id);
    }

    /**
     * Convert a proposal model to a DTO.
     *
     * @param model The proposal model.
     * @return The proposal DTO.
     */
    private proposalDto convertToDto(proposalModel model) {
        return new proposalDto(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getSalary(),
                model.getStatus(),
                model.getEmail()
        );
    }

    /**
     * Convert a proposal DTO to a model.
     *
     * @param dto The proposal DTO.
     * @return The proposal model.
     */
    private proposalModel convertToModel(proposalDto dto) {
        proposalModel model = new proposalModel();
        model.setTitle(dto.title());
        model.setDescription(dto.description());
        model.setSalary(dto.salary());
        model.setStatus(dto.status());
        model.setEmail(dto.email());
        return model;
    }
}