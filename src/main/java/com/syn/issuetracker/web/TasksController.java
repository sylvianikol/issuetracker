package com.syn.issuetracker.web;

import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.service.TaskServiceModel;
import com.syn.issuetracker.model.view.TaskViewModel;
import com.syn.issuetracker.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Autowired
    public TasksController(TaskService taskService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<Set<TaskViewModel>> getAll( @RequestParam(required = false, name = "title") String title) {

        Set<TaskViewModel> tasks = this.taskService.getAll(title)
                .stream()
                .map(task -> this.modelMapper.map(task, TaskViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return tasks.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(tasks);
    }

    @CrossOrigin
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskViewModel> get(@PathVariable String taskId) {

        Optional<TaskServiceModel> task = this.taskService.get(taskId);

        return task.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(task.get(), TaskViewModel.class));
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<?> addConfirm(@Valid @RequestBody TaskAddBindingModel taskAddBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(taskAddBindingModel);
        }

        TaskServiceModel task = this.taskService.add(taskAddBindingModel);

        return ResponseEntity.created(uriComponentsBuilder.path("/tasks/{taskId}")
                .buildAndExpand(task.getId())
                .toUri()).build();
    }

    @CrossOrigin
    @PutMapping("{taskId}")
    public ResponseEntity<?> edit(@PathVariable String taskId,
                                  @Valid @RequestBody TaskEditBindingModel taskEditBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(taskEditBindingModel);
        }

        TaskServiceModel task = this.taskService.edit(taskEditBindingModel, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("api/tasks/{taskId}")
                        .buildAndExpand(task.getId())
                        .toUri())
                .build();
    }

    @CrossOrigin
    @DeleteMapping("{taskId}")
    public ResponseEntity<?> delete(@PathVariable String taskId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.taskService.delete(taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/tasks").build().toUri())
                .build();
    }

    @CrossOrigin
    @DeleteMapping("")
    public ResponseEntity<?> deleteAll() {

        this.taskService.deleteAll();

        return ResponseEntity.ok().build();
    }
}
