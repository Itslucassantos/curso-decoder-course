package com.ead.course.dtos;



import javax.validation.constraints.NotNull;
import java.util.UUID;

public class SubscriptionDto {

    @NotNull
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
