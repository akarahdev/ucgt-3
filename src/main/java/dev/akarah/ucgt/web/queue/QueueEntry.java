package dev.akarah.ucgt.web.queue;

public record QueueEntry(
        int id,
        String method,
        String endpoint,
        String body
) {
}
