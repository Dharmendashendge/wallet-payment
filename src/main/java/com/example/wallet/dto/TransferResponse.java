package com.example.wallet.dto;

import org.springframework.hateoas.RepresentationModel;

public class TransferResponse extends RepresentationModel<TransferResponse> {

    private String content;
    private Long transferId;

    public TransferResponse() {
    }

    public TransferResponse(String content, Long transferId) {
        this.content = content;
        this.transferId = transferId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
}