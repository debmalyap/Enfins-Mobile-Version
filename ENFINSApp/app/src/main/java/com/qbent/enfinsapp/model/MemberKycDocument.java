package com.qbent.enfinsapp.model;

import android.graphics.Bitmap;

import java.util.List;

public class MemberKycDocument {
    private String id;
    private String memberId;
    private String kycdocumentId;
    private String documentType;
    private String kycdocumentNo;
    private String kycdocumentScan;
    private String kycdocumentName;
    private String issueDate;
    private String expiryDate;
    private String remarks;
    private Bitmap bitmap;
    private String json;
    private String image;
    private List<Documents> documents;

    public MemberKycDocument(String id, String memberId, String kycdocumentId, String documentType, String kycdocumentNo, String kycdocumentScan, String kycdocumentName, String issueDate, String expiryDate, String remarks, Bitmap bitmap, String json, String image, List<Documents> documents) {
        this.id = id;
        this.memberId = memberId;
        this.kycdocumentId = kycdocumentId;
        this.documentType = documentType;
        this.kycdocumentNo = kycdocumentNo;
        this.kycdocumentScan = kycdocumentScan;
        this.kycdocumentName = kycdocumentName;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.remarks = remarks;
        this.documents = documents;
        this.bitmap = bitmap;
        this.json = json;
        this.image = image;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getKycdocumentId() {
        return kycdocumentId;
    }

    public void setKycdocumentId(String kycdocumentId) {
        this.kycdocumentId = kycdocumentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getKycdocumentNo() {
        return kycdocumentNo;
    }

    public void setKycdocumentNo(String kycdocumentNo) {
        this.kycdocumentNo = kycdocumentNo;
    }

    public String getKycdocumentScan() {
        return kycdocumentScan;
    }

    public void setKycdocumentScan(String kycdocumentScan) {
        this.kycdocumentScan = kycdocumentScan;
    }

    public String getKycdocumentName() {
        return kycdocumentName;
    }

    public void setKycdocumentName(String kycdocumentName) {
        this.kycdocumentName = kycdocumentName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
