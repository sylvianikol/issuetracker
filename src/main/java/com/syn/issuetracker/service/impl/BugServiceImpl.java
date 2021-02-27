package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.model.binding.BugAddBindingModel;
import com.syn.issuetracker.model.service.BugServiceModel;
import com.syn.issuetracker.repository.BugRepository;
import com.syn.issuetracker.service.BugService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BugServiceImpl(BugRepository bugRepository, ModelMapper modelMapper) {
        this.bugRepository = bugRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BugServiceModel add(BugAddBindingModel bugAddBindingModel) {

        return null;
    }
}
