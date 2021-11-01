package com.ss.eastcoderbank.loanapi.dto;

import com.ss.eastcoderbank.core.model.loan.LoanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanDto {

    @NotNull
    private LoanType loanType;

    @NotNull
    @Min(value = 0, message = "Amount loaned can not be negative.")
    private Float amountLoaned;

    @NotNull
    private List<Integer> usersIds;

    @NotBlank
    @Size(max = 20, message = "name must be less then 20 characters")
    private String nickName;
}
