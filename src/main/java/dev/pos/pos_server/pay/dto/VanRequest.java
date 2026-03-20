package dev.pos.pos_server.pay.dto;

import lombok.Builder;

@Builder
public record VanRequest(
        String rawIsoMessage
) {}
