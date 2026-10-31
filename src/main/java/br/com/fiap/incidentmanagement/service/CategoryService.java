package br.com.fiap.incidentmanagement.service;

import br.com.fiap.incidentmanagement.dto.CategoryData;
import br.com.fiap.incidentmanagement.dto.IncidentTypeData;
import br.com.fiap.incidentmanagement.entity.Category;
import br.com.fiap.incidentmanagement.entity.IncidentType;
import br.com.fiap.incidentmanagement.repository.CategoryRepository;
import br.com.fiap.incidentmanagement.repository.IncidentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IncidentTypeRepository typeRepository;

    public List<String> findCategories() {
        return categoryRepository.findAll().stream().map(Category::getName).toList();
    }

    @Transactional
    public void createCategory(CategoryData data) {
        Category category = new Category();
        category.setName(data.name().toUpperCase());
        categoryRepository.save(category);
    }

    public List<String> findTypes(String categoryName) {
        Category category = findCategory(categoryName);
        return typeRepository.findAllByCategory(category).stream().map(IncidentType::getName).toList();
    }

    @Transactional
    public void createType(IncidentTypeData data) {
        IncidentType type = new IncidentType();
        type.setName(data.name().toUpperCase());
        type.setCategory(findCategory(data.category()));
        typeRepository.save(type);
    }

    public Category findCategory(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + name));
    }

    public IncidentType findType(String name, Category category) {
        return typeRepository.findByNameAndCategory(name, category)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de ocorrência não encontrado para a categoria: " + name));
    }
}
