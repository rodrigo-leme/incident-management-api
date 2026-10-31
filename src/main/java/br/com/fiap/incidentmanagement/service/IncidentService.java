package br.com.fiap.incidentmanagement.service;

import br.com.fiap.incidentmanagement.dto.*;
import br.com.fiap.incidentmanagement.entity.*;
import br.com.fiap.incidentmanagement.repository.IncidentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository repository;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CategoryService categoryService;

    @Transactional
    public IncidentResponse create(IncidentCreateData data, User loggedUser) {
        Incident incident = new Incident();
        fillData(incident, data.idSchoolClass(), data.idStudent(), data.category(), data.type(), data.description());
        incident.setUser(loggedUser);
        incident.setRegisterDate(data.registerDate());
        incident.setStatus(Status.AGUARDANDO);
        incident.setDeleted(false);

        repository.save(incident);
        return new IncidentResponse(incident);
    }

    public Page<IncidentResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(IncidentResponse::new);
    }

    public IncidentResponse findById(Long id) {
        return new IncidentResponse(findIncident(id));
    }

    public List<IncidentResponse> search(Long idStudent, Long idSchoolClass, String category, String type,
                                         LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate == null ? null : startDate.atStartOfDay();
        LocalDateTime end = endDate == null ? null : endDate.atTime(23, 59, 59);

        return repository.search(idStudent, idSchoolClass, category, type, start, end)
                .stream().map(IncidentResponse::new).toList();
    }

    @Transactional
    public IncidentResponse update(IncidentUpdateData data, User loggedUser) {
        Incident incident = findIncident(data.id());
        checkOwner(incident, loggedUser);
        fillData(incident, data.idSchoolClass(), data.idStudent(), data.category(), data.type(), data.description());
        return new IncidentResponse(incident);
    }

    @Transactional
    public void delete(Long id, User loggedUser) {
        Incident incident = findIncident(id);
        checkOwner(incident, loggedUser);
        incident.setDeleted(true);
    }

    public List<StatusData> findStatuses() {
        return Arrays.stream(Status.values())
                .map(status -> new StatusData(status.name(), status.getDescription()))
                .toList();
    }

    @Transactional
    public IncidentResponse updateStatus(IncidentStatusData data) {
        Incident incident = findIncident(data.id());
        incident.setStatus(data.status());
        incident.setUpdateDate(data.updateDate());
        return new IncidentResponse(incident);
    }

    public Page<IncidentResponse> findByStatus(String group, Pageable pageable) {
        List<Status> statuses = switch (group) {
            case "waiting" -> List.of(Status.AGUARDANDO);
            case "progressing" -> List.of(Status.EM_ATENDIMENTO);
            case "active" -> List.of(Status.AGUARDANDO, Status.EM_ATENDIMENTO);
            case "solved" -> List.of(Status.RESOLVIDA);
            case "unsolved" -> List.of(Status.ENCERRADA_SEM_SOLUCAO);
            case "closed" -> List.of(Status.RESOLVIDA, Status.ENCERRADA_SEM_SOLUCAO);
            default -> throw new IllegalArgumentException("Status inválido: " + group);
        };

        return repository.findAllByStatusInAndDeletedFalse(statuses, pageable).map(IncidentResponse::new);
    }

    public SummaryData summary() {
        List<Incident> incidents = repository.findAllByDeletedFalse();
        DateTimeFormatter periodFormat = DateTimeFormatter.ofPattern("MM/yyyy");

        return new SummaryData(
                count(incidents, Status.AGUARDANDO, Status.EM_ATENDIMENTO),
                count(incidents, Status.AGUARDANDO),
                count(incidents, Status.EM_ATENDIMENTO),
                count(incidents, Status.RESOLVIDA),
                count(incidents, Status.ENCERRADA_SEM_SOLUCAO),
                count(incidents, Status.RESOLVIDA, Status.ENCERRADA_SEM_SOLUCAO),
                group(incidents, incident -> incident.getCategory().getName()),
                group(incidents, incident -> incident.getType().getName()),
                group(incidents, incident -> incident.getSchoolClass().getName()),
                group(incidents, incident -> incident.getStudent().getName()),
                group(incidents, incident -> incident.getRegisterDate().format(periodFormat)));
    }

    private void fillData(Incident incident, Long idSchoolClass, Long idStudent, String categoryName,
                          String typeName, String description) {
        SchoolClass schoolClass = schoolClassService.findSchoolClass(idSchoolClass);
        Student student = studentService.findStudent(idStudent);

        if (!schoolClass.getStudents().contains(student)) {
            throw new IllegalArgumentException("O aluno não pertence à turma informada.");
        }

        Category category = categoryService.findCategory(categoryName);
        IncidentType type = categoryService.findType(typeName, category);

        incident.setSchoolClass(schoolClass);
        incident.setStudent(student);
        incident.setCategory(category);
        incident.setType(type);
        incident.setDescription(description);
    }

    private void checkOwner(Incident incident, User loggedUser) {
        if (!incident.getUser().getId().equals(loggedUser.getId())) {
            throw new AccessDeniedException("Somente o professor que registrou a ocorrência pode alterá-la ou excluí-la.");
        }
    }

    private Incident findIncident(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private Long count(List<Incident> incidents, Status... statuses) {
        List<Status> statusList = Arrays.asList(statuses);
        return incidents.stream().filter(incident -> statusList.contains(incident.getStatus())).count();
    }

    // Agrupa e conta as ocorrências pela chave informada, da maior para a menor quantidade (ranking)
    private List<KeyValueData> group(List<Incident> incidents, Function<Incident, String> key) {
        Map<String, Long> totals = incidents.stream().collect(Collectors.groupingBy(key, Collectors.counting()));

        return totals.entrySet().stream()
                .map(entry -> new KeyValueData(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(KeyValueData::value).reversed())
                .toList();
    }
}
