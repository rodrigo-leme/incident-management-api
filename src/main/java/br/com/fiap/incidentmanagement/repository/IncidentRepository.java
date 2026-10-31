package br.com.fiap.incidentmanagement.repository;

import br.com.fiap.incidentmanagement.entity.Incident;
import br.com.fiap.incidentmanagement.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    Page<Incident> findAllByStatusInAndDeletedFalse(List<Status> statuses, Pageable pageable);

    List<Incident> findAllByDeletedFalse();

    @Query("""
            select i from Incident i
            where i.deleted = false
            and (:idStudent is null or i.student.id = :idStudent)
            and (:idSchoolClass is null or i.schoolClass.id = :idSchoolClass)
            and (:category is null or i.category.name = :category)
            and (:type is null or i.type.name = :type)
            and (:startDate is null or i.registerDate >= :startDate)
            and (:endDate is null or i.registerDate <= :endDate)
            order by i.registerDate desc
            """)
    List<Incident> search(@Param("idStudent") Long idStudent,
                          @Param("idSchoolClass") Long idSchoolClass,
                          @Param("category") String category,
                          @Param("type") String type,
                          @Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);
}
