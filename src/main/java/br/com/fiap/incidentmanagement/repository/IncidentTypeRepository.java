package br.com.fiap.incidentmanagement.repository;

import br.com.fiap.incidentmanagement.entity.Category;
import br.com.fiap.incidentmanagement.entity.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentTypeRepository extends JpaRepository<IncidentType, Long> {

    List<IncidentType> findAllByCategory(Category category);

    Optional<IncidentType> findByNameAndCategory(String name, Category category);
}
