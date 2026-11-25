package dev.akarah.ucgt.web.queue;

public record QueueResponse(
        int id,
        int status,
        String body
) {
}
