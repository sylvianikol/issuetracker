package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.exception.CustomEntityNotFoundException;
import com.syn.issuetracker.exception.DataConflictException;
import com.syn.issuetracker.exception.UnprocessableEntityException;
import com.syn.issuetracker.exception.error.ErrorContainer;
import com.syn.issuetracker.model.binding.DeveloperAddBindingModel;
import com.syn.issuetracker.model.entity.Developer;
import com.syn.issuetracker.model.service.DeveloperServiceModel;
import com.syn.issuetracker.repository.DeveloperRepository;
import com.syn.issuetracker.service.DeveloperService;
import com.syn.issuetracker.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public DeveloperServiceImpl(DeveloperRepository developerRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.developerRepository = developerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public DeveloperServiceModel add(DeveloperAddBindingModel developerAddBindingModel) {

        if (!this.validationUtil.isValid(developerAddBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(developerAddBindingModel));
        }

        String name = developerAddBindingModel.getName();
        if (this.developerRepository.findByName(name).isPresent()) {
            throw new DataConflictException(DATA_CONFLICT, new ErrorContainer(Map.of("alreadyExists",
                    Set.of(String.format(DEV_ALREADY_EXISTS, name)))));
        }

        Developer developer = this.modelMapper.map(developerAddBindingModel, Developer.class);

        this.developerRepository.save(developer);

        return this.modelMapper.map(developer, DeveloperServiceModel.class);
    }

    @Override
    public Optional<DeveloperServiceModel> get(String developerId) {
        Optional<Developer> developer = this.developerRepository.findById(developerId);

        return developer.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(developer.get(), DeveloperServiceModel.class));
    }

    @Override
    public Set<DeveloperServiceModel> getAll() {
        Set<DeveloperServiceModel> developers = this.developerRepository
                .findAll().stream()
                .map(d -> this.modelMapper.map(d, DeveloperServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(developers);
    }

    @Override
    public DeveloperServiceModel edit(DeveloperAddBindingModel developerAddBindingModel, String developerId) {

        Developer developer = this.developerRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(DEVELOPER_NOT_FOUND); });

        if (!this.validationUtil.isValid(developerAddBindingModel)) {
            throw new UnprocessableEntityException(VALIDATION_FAILED,
                    this.validationUtil.getViolations(developerAddBindingModel));
        }

        developer.setName(developerAddBindingModel.getName());
        this.developerRepository.save(developer);

        return this.modelMapper.map(developer, DeveloperServiceModel.class);
    }

    @Override
    public void delete(String developerId) {
        Developer developer = this.developerRepository.findById(developerId)
                .orElseThrow(() -> { throw new CustomEntityNotFoundException(DEVELOPER_NOT_FOUND); });

        this.developerRepository.delete(developer);
    }

}
