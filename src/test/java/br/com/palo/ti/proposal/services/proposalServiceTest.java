package br.com.palo.ti.proposal.services;

import br.com.palo.ti.proposal.dtos.proposalDto;
import br.com.palo.ti.proposal.models.proposalModel;
import br.com.palo.ti.proposal.producers.candidateProducer;
import br.com.palo.ti.proposal.repositories.proposalRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class proposalServiceTest {

    @Mock
    private proposalRepository repository;

    @Mock
    private candidateProducer producer;
    
    @Mock
    private proposalModel model1;

    @Mock
    private proposalModel model2;

    @InjectMocks
    private proposalService service;

    @BeforeEach()
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
    }

        @Test
    void getAllProposalsReturnsListOfProposals() {
        List<proposalModel> models = List.of(
                model1,
                model2
        );
        when(repository.findAll()).thenReturn(models);
        when(model1.getTitle()).thenReturn("Title1");
        when(model2.getTitle()).thenReturn("Title2");

        List<proposalDto> result = service.getAllProposals();

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).title());
        assertEquals("Title2", result.get(1).title());
    }

    @Test
    void getProposalByIdReturnsProposalWhenFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(model1));
        when(model1.getTitle()).thenReturn("Title");
        when(model1.getDescription()).thenReturn("Description");

        proposalDto result = service.getProposalById(id);

        assertEquals("Title", result.title());
        assertEquals("Description", result.description());
    }

    @Test
    void getProposalByIdThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getProposalById(id));
    }

    @Test
    void createProposalSavesAndReturnsProposal() {
        // Arrange
        proposalDto dto = new proposalDto(null, "Title1", "Description", 50000.0, "Open", "email@example.com");
        proposalModel savedModel = model1;
        when(repository.save(any(proposalModel.class))).thenReturn(savedModel);
        when(savedModel.getTitle()).thenReturn("Title1");
        when(savedModel.getDescription()).thenReturn("Description");

        doNothing().when(producer).publishStatus(any());

        // Act
        proposalDto result = service.createProposal(dto);

        // Assert
        verify(repository).save(any(proposalModel.class));
        assertEquals("Title1", result.title());
        assertEquals("Description", result.description());
    }

    @Test
    void updateProposalUpdatesAndReturnsProposal() {
        UUID id = UUID.randomUUID();
        proposalDto dto = new proposalDto(null, "Updated Title", "Updated Description", 60000.0, "Closed", "updated@example.com");
        proposalModel updatedModel = Mockito.mock(proposalModel.class);

        when(updatedModel.getId()).thenReturn(id);
        when(updatedModel.getTitle()).thenReturn("Updated Title");
        when(updatedModel.getDescription()).thenReturn("Updated Description");
        when(updatedModel.getSalary()).thenReturn(50000.0);
        when(updatedModel.getStatus()).thenReturn("Open");
        when(updatedModel.getEmail()).thenReturn("email1@example.com");

        when(repository.findById(id)).thenReturn(Optional.of(model1));
        when(repository.save(model1)).thenReturn(updatedModel);

        doNothing().when(producer).publishStatus(any());

        proposalDto result = service.updateProposal(id, dto);

        assertEquals("Updated Title", result.title());
        assertEquals("Updated Description", result.description());
    }

    @Test
    void updateProposalThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        proposalDto dto = new proposalDto(null, "Updated Title", "Updated Description", 60000.0, "Closed", "updated@example.com");
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateProposal(id, dto));
    }

    @Test
    void deleteProposalDeletesWhenFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        assertDoesNotThrow(() -> service.deleteProposal(id));
    }

    @Test
    void deleteProposalThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deleteProposal(id));
    }
}
