package com.syn.issuetracker.payload.response;

import com.syn.issuetracker.model.service.UserServiceModel;

public class SignUpResponse {

    private UserServiceModel user;
    private MessageResponse messageResponse;

    public SignUpResponse(UserServiceModel user, MessageResponse messageResponse) {
        this.user = user;
        this.messageResponse = messageResponse;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }
}
