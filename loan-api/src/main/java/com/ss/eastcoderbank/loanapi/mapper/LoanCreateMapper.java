package com.ss.eastcoderbank.loanapi.mapper;

import com.ss.eastcoderbank.core.model.account.Account;
import com.ss.eastcoderbank.core.model.loan.Loan;
import com.ss.eastcoderbank.loanapi.dto.CreateLoanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanCreateMapper {

    Loan mapToEntity(CreateLoanDto loanCreateDto);

}