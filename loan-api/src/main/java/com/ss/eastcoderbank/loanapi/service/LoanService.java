package com.ss.eastcoderbank.loanapi.service;

import com.ss.eastcoderbank.core.exeception.UserNotFoundException;
import com.ss.eastcoderbank.core.model.account.Account;
import com.ss.eastcoderbank.core.model.loan.Loan;
import com.ss.eastcoderbank.core.model.user.User;
import com.ss.eastcoderbank.core.repository.LoanRepository;
import com.ss.eastcoderbank.core.repository.UserRepository;
import com.ss.eastcoderbank.core.transferdto.LoanDto;
import com.ss.eastcoderbank.core.transfermapper.LoanMapper;
import com.ss.eastcoderbank.loanapi.dto.CreateLoanDto;
import com.ss.eastcoderbank.loanapi.mapper.LoanCreateMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LoanService{
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;
    private final LoanCreateMapper loanCreateMapper;

    public Page<LoanDto> getLoans(Integer pageNumber, Integer pageSize, boolean asc, String sort) {
        log.info("Request for paginated results of all loans...");
        Page<LoanDto> req;
        if (sort != null) {
            log.info("Results sorted by {}...", sort);
            req = loanRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort))).map(loan -> loanMapper.mapToDto(loan));
        } else {
            log.info("Results not sorted...");
            req = loanRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(loan -> loanMapper.mapToDto(loan));
        }
        log.info("Returning Page of LoanDto...");
        return req;
    }

    public Page<LoanDto> getLoansByUser(Integer userId, Integer pageNumber, Integer pageSize, boolean asc, String sort) {
        log.info("Request for paginated results of user's loans with id: {}...", userId);
        Page<LoanDto> req;
        if (sort != null) {
            log.info("Results sorted by {}...", sort);
            req = loanRepository.findLoanByUsers(userId, PageRequest.of(pageNumber, pageSize, Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort))).map(loan -> loanMapper.mapToDto(loan));
        } else {
            log.info("Results not sorted...");
            req = loanRepository.findLoanByUsers(userId, PageRequest.of(pageNumber, pageSize)).map(loan -> loanMapper.mapToDto(loan));
        }
        log.info("Returning Page of LoanDto...");
        return req;
    }

    public void createLoan(CreateLoanDto payload) {

        log.info("Request for new loan...");

        log.info("Mapping request DTO to loan object...");
        Loan loan = loanCreateMapper.mapToEntity(payload);

        log.info("Transforming user ids into user list...");
        List<User> users = new ArrayList<>();
        payload.getUsersIds().forEach(integer -> users.add(userRepository.findById(integer).orElseThrow(UserNotFoundException::new)));

        log.info("Applying default loan setting to new loan...");
        loan.setUsers(users);

        log.info("Saving new loan to database...");
        loanRepository.save(loan);

    }

}