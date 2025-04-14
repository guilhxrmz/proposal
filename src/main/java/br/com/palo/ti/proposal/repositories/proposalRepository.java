package br.com.palo.ti.proposal.repositories;

import br.com.palo.ti.proposal.models.proposalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface proposalRepository extends JpaRepository<proposalModel, UUID> {
}