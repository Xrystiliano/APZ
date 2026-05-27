package com.example.smartlock.model.dto.accesskey;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateKeyRequest {
    private OffsetDateTime validFrom;
    private OffsetDateTime validUntil;


    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public OffsetDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(OffsetDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
