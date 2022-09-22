package com.loan.api.rest.controller;

import com.loan.api.rest.domain.LoanRequest;
import com.loan.api.rest.domain.LoanResponse;
import com.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> applyLoan(@Valid @RequestBody LoanRequest loanRequest, HttpServletRequest request) {
        var loanResponse = loanService.applyLoan(loanRequest.getFrom(), loanRequest.getTo(), loanRequest.getAmount());
        return ResponseEntity.created(URI.create(request.getServletPath().concat("/").concat(loanResponse.getLoanId())))
                .body(loanResponse);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }

    @PatchMapping("/{loanId}")
    public ResponseEntity<LoanResponse> extendLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.extendLoan(loanId));
    }

}
