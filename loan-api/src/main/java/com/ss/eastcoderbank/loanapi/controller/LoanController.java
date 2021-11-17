package com.ss.eastcoderbank.loanapi.controller;

import com.ss.eastcoderbank.core.exeception.UserNotFoundException;
import com.ss.eastcoderbank.core.model.user.User;
import com.ss.eastcoderbank.core.repository.UserRepository;
import com.ss.eastcoderbank.core.transferdto.LoanDto;
import com.ss.eastcoderbank.loanapi.dto.CreateLoanDto;
import com.ss.eastcoderbank.loanapi.service.LoanService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("loans")
@NoArgsConstructor
public class LoanController {

    @Autowired
    LoanService loanService;
    @Autowired
    UserRepository userRepository;
    private MeterRegistry meterRegistry;
    private Counter serviceCounter;
    private Timer serviceTimer;

    LoanController(MeterRegistry meterRegistry){
        this.meterRegistry = meterRegistry;
        serviceCounter = meterRegistry.counter("PAGE_VIEWS.Loans");
        serviceTimer = meterRegistry.timer("execution.time.Loans");
    }

    @GetMapping
    public ResponseEntity<Page<LoanDto>> getPaginatedLoans(@RequestParam(name="page") Integer pageNumber, @RequestParam(name="size") Integer pageSize, @RequestParam(value="asc", required = false) boolean asc, @RequestParam(value = "sort", required = false) String sort, Pageable page) {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("get loan endpoint reached...");
        log.debug("Endpoint pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
        return new ResponseEntity<>(loanService.getLoans(pageNumber, pageSize, asc, sort), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/page")
    public ResponseEntity<Page<LoanDto>> getPaginatedLoansByUser(@PathVariable Integer id, @RequestParam(name="page") Integer pageNumber, @RequestParam(name="size") Integer pageSize, @RequestParam(value="asc", required = false) boolean asc, @RequestParam(value = "sort", required = false) String sort, Pageable page) {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("get loans by user endpoint reached...");
        log.info("user id: {}", id);
        log.debug("Endpoint pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
        return new ResponseEntity<>(loanService.getLoansByUserPaginated(user, pageNumber, pageSize, asc, sort), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<LoanDto>> getLoansByUser(@PathVariable Integer id) {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("get loans by user endpoint reached...");
        log.info("user id: {}", id);
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
        return new ResponseEntity<>(loanService.getLoansByUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoansById(@PathVariable Integer id) {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("get loans by id endpoint reached...");
        log.info("loan id: {}", id);
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
        return new ResponseEntity<>(loanService.getLoansById(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLoan(@Valid @RequestBody CreateLoanDto loan) {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("POST loan endpoint reached...");
        loanService.createLoan(loan);
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {
        long startTime = System.currentTimeMillis();
        serviceCounter.increment();
        log.trace("HEALTH loan endpoint reached...");
        serviceTimer.record(Duration.ofMillis((System.currentTimeMillis()-startTime)));
    }

}
