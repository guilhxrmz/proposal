package br.com.palo.ti.proposal.controllers;

import br.com.palo.ti.proposal.dtos.proposalDto;
import br.com.palo.ti.proposal.services.proposalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class proposalControllerTest {

    @Mock
    private proposalService service;

    @InjectMocks
    private proposalController controller;

    @Test
    void getAllProposalsReturnsListOfProposals() {
        List<proposalDto> proposals = List.of(
                new proposalDto(UUID.randomUUID(), "Title1", "Description1", 50000.0, "Open", "email1@example.com"),
                new proposalDto(UUID.randomUUID(), "Title2", "Description2", 60000.0, "Closed", "email2@example.com")
        );
        when(service.getAllProposals()).thenReturn(proposals);

        ResponseEntity<List<proposalDto>> response = controller.getAllProposals();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposals, response.getBody());
    }

    @Test
    void getProposalByIdReturnsProposalWhenFound() {
        UUID id = UUID.randomUUID();
        proposalDto proposal = new proposalDto(id, "Title", "Description", 50000.0, "Open", "email@example.com");
        when(service.getProposalById(id)).thenReturn(proposal);

        ResponseEntity<proposalDto> response = controller.getProposalById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    void getProposalByIdThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(service.getProposalById(id)).thenThrow(new RuntimeException("Proposal not found"));

        assertThrows(RuntimeException.class, () -> controller.getProposalById(id));
    }

    @Test
    void createProposalReturnsCreatedProposal() {
        proposalDto inputDto = new proposalDto(null, "Title", "Description", 50000.0, "Open", "email@example.com");
        proposalDto createdDto = new proposalDto(UUID.randomUUID(), "Title", "Description", 50000.0, "Open", "email@example.com");
        when(service.createProposal(inputDto)).thenReturn(createdDto);

        ResponseEntity<proposalDto> response = controller.createProposal(inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
    }

    @Test
    void updateProposalReturnsUpdatedProposal() {
        UUID id = UUID.randomUUID();
        proposalDto inputDto = new proposalDto(null, "Updated Title", "Updated Description", 60000.0, "Closed", "updated@example.com");
        proposalDto updatedDto = new proposalDto(id, "Updated Title", "Updated Description", 60000.0, "Closed", "updated@example.com");
        when(service.updateProposal(id, inputDto)).thenReturn(updatedDto);

        ResponseEntity<proposalDto> response = controller.updateProposal(id, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void deleteProposalReturnsNoContentWhenSuccessful() {
        UUID id = UUID.randomUUID();
        doNothing().when(service).deleteProposal(id);

        ResponseEntity<Void> response = controller.deleteProposal(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteProposalThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Proposal not found")).when(service).deleteProposal(id);

        assertThrows(RuntimeException.class, () -> controller.deleteProposal(id));
    }
}
