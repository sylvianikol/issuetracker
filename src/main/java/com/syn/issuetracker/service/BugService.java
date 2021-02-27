package com.syn.issuetracker.service;

import com.syn.issuetracker.model.binding.BugAddBindingModel;
import com.syn.issuetracker.model.service.BugServiceModel;

public interface BugService {

    BugServiceModel add(BugAddBindingModel bugAddBindingModel);
}
