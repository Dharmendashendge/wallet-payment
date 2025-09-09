package com.example.wallet.dto;

import org.springframework.hateoas.RepresentationModel;

public class CreateUserResponse extends RepresentationModel<CreateUserResponse> {

    private String content;

    public CreateUserResponse() {
    }

    public CreateUserResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}