package com.qbent.enfinsapp.model;

import java.util.List;

public class LoanHouseVisit {
    private String ApplicationNo;
    private String BorrowerName;
    private String LoanProduct;
    private String LoanPurpose;

    private List<LoanHouseVisitDetail> detail;

    public LoanHouseVisit(String applicationNo, String borrowerName, String loanProduct, String loanPurpose, List<LoanHouseVisitDetail> detail) {
        ApplicationNo = applicationNo;
        BorrowerName = borrowerName;
        LoanProduct = loanProduct;
        LoanPurpose = loanPurpose;
        this.detail = detail;
    }

    public String getApplicationNo() {
        return ApplicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        ApplicationNo = applicationNo;
    }

    public String getBorrowerName() {
        return BorrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        BorrowerName = borrowerName;
    }

    public String getLoanProduct() {
        return LoanProduct;
    }

    public void setLoanProduct(String loanProduct) {
        LoanProduct = loanProduct;
    }

    public String getLoanPurpose() {
        return LoanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        LoanPurpose = loanPurpose;
    }

    public List<LoanHouseVisitDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<LoanHouseVisitDetail> detail) {
        this.detail = detail;
    }
}
