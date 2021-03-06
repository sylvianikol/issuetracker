package com.syn.issuetracker.web;

import com.syn.issuetracker.exception.error.ErrorResponse;
import com.syn.issuetracker.specification.TaskSpecification;
import com.syn.issuetracker.model.binding.TaskAddBindingModel;
import com.syn.issuetracker.model.binding.TaskEditBindingModel;
import com.syn.issuetracker.model.service.TaskServiceModel;
import com.syn.issuetracker.model.view.TaskViewModel;
import com.syn.issuetracker.service.TaskService;
import com.syn.issuetracker.utils.extractor.BindingResultErrorExtractor;
import com.syn.issuetracker.utils.extractor.ErrorExtractor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.*;

import static com.syn.issuetracker.common.ExceptionErrorMessages.*;
import static com.syn.issuetracker.common.MiscConstants.TOTAL_ITEMS;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;
    private ErrorExtractor<BindingResult, String> errorExtractor;

    @Autowired
    public TasksController(TaskService taskService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.modelMapper = modelMapper;
        this.errorExtractor = new BindingResultErrorExtractor();
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false, name = "title") String title,
                                                      @RequestParam(name = "userId") String userId,
                                                      @PageableDefault(sort = {"status", "priority","createdOn"},
                                                              direction = Sort.Direction.ASC) Pageable pageable) {

        Map<String, Object> response = this.taskService.getAll(new TaskSpecification(userId, title), pageable);

        return (long) response.get(TOTAL_ITEMS) == 0L
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", NO_TASKS_FOUND))
                : ResponseEntity.ok().body(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskViewModel> get(@PathVariable String taskId) {

        Optional<TaskServiceModel> task = this.taskService.get(taskId);

        return task.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(task.get(), TaskViewModel.class));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody TaskAddBindingModel taskAddBindingModel,
                                 BindingResult bindingResult) throws MessagingException, InterruptedException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(422, VALIDATION_FAILURE,
                            this.errorExtractor.getViolations(bindingResult)));
        }

        this.taskService.add(taskAddBindingModel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("{taskId}")
    public ResponseEntity<?> edit(@PathVariable String taskId,
                                  @Valid @RequestBody TaskEditBindingModel taskEditBindingModel,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(422, VALIDATION_FAILURE,
                            this.errorExtractor.getViolations(bindingResult)));
        }

        this.taskService.edit(taskEditBindingModel, taskId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> delete(@PathVariable String taskId) {

        this.taskService.delete(taskId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAll(@RequestParam(name = "userId") String userId) {

        this.taskService.deleteAll(userId);

        return ResponseEntity.ok().build();
    }
}
