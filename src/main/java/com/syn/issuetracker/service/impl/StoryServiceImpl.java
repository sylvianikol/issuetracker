package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.repository.StoryRepository;
import com.syn.issuetracker.service.StoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository, ModelMapper modelMapper) {
        this.storyRepository = storyRepository;
        this.modelMapper = modelMapper;
    }
}
