package com.syn.issuetracker.service;

import com.syn.issuetracker.model.binding.DeveloperAddBindingModel;
import com.syn.issuetracker.model.service.DeveloperServiceModel;

import java.util.Optional;
import java.util.Set;

public interface DeveloperService {
    DeveloperServiceModel add(DeveloperAddBindingModel developerAddBindingModel);

    Optional<DeveloperServiceModel> get(String developerId);

    Set<DeveloperServiceModel> getAll();

    DeveloperServiceModel edit(DeveloperAddBindingModel developerAddBindingModel, String developerId);

    void delete(String developerId);
}
