package com.syn.issuetracker.web;

import com.syn.issuetracker.model.binding.DeveloperAddBindingModel;
import com.syn.issuetracker.model.service.DeveloperServiceModel;
import com.syn.issuetracker.model.view.DeveloperViewModel;
import com.syn.issuetracker.service.DeveloperService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/developers")
@CrossOrigin(origins = "http://localhost:4200")
public class DeveloperController {

    private final DeveloperService developerService;
    private final ModelMapper modelMapper;

    @Autowired
    public DeveloperController(DeveloperService developerService, ModelMapper modelMapper) {
        this.developerService = developerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    ResponseEntity<Set<DeveloperViewModel>> getAll() {

        Set<DeveloperViewModel> developers = this.developerService.getAll().stream()
                .map(r -> this.modelMapper.map(r, DeveloperViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return developers.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(developers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperViewModel> get(@PathVariable String id) {

        Optional<DeveloperServiceModel> developer =
                this.developerService.get(id);

        return developer.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(developer.get(), DeveloperViewModel.class));

    }

    @PostMapping("add")
    public ResponseEntity<?> add(@Valid @RequestBody DeveloperAddBindingModel developerAddBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(developerAddBindingModel);
        }

        DeveloperServiceModel developer = this.developerService.add(developerAddBindingModel);

        return ResponseEntity.created(uriComponentsBuilder.path("/developers/{developerId}")
                .buildAndExpand(developer.getId())
                .toUri()).build();
    }

    @PutMapping("/{developerId}")
    public ResponseEntity<?> edit(@PathVariable String developerId,
                                  @Valid @RequestBody DeveloperAddBindingModel developerAddBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(developerAddBindingModel);
        }

        DeveloperServiceModel developer = this.developerService.edit(developerAddBindingModel, developerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/developers/{developerId}")
                        .buildAndExpand(developer.getId())
                        .toUri())
                .build();
    }

    @DeleteMapping("/{developerId}")
    public ResponseEntity<?> delete(@PathVariable String developerId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.developerService.delete(developerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path("/developers").build().toUri())
                .build();
    }
}
