package com.ss.eastcoderbank.loanapi.controller;

import com.ss.eastcoderbank.core.transferdto.LoanDto;
import com.ss.eastcoderbank.loanapi.dto.CreateLoanDto;
import com.ss.eastcoderbank.loanapi.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("loans")
public class LoanController {

    @Autowired
    LoanService loanService;

    @GetMapping
    public ResponseEntity<Page<LoanDto>> getPaginatedLoans(@RequestParam(name="page") Integer pageNumber, @RequestParam(name="size") Integer pageSize, @RequestParam(value="asc", required = false) boolean asc, @RequestParam(value = "sort", required = false) String sort, Pageable page) {
        log.trace("get loan endpoint reached...");
        log.debug("Endpoint pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return new ResponseEntity<>(loanService.getLoans(pageNumber, pageSize, asc, sort), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Page<LoanDto>> getPaginatedLoansByUser(@PathVariable Integer userId, @RequestParam(name="page") Integer pageNumber, @RequestParam(name="size") Integer pageSize, @RequestParam(value="asc", required = false) boolean asc, @RequestParam(value = "sort", required = false) String sort, Pageable page) {
        log.trace("get loans by user endpoint reached...");
        log.debug("Endpoint pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return new ResponseEntity<>(loanService.getLoansByUser(userId, pageNumber, pageSize, asc, sort), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLoan(@Valid @RequestBody CreateLoanDto loan) {
        log.trace("POST loan endpoint reached...");
        loanService.createLoan(loan);
    }

}
