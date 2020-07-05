package toolkit.instrumentation.asm.calltraces.amplifix;


import toolkit.factory.domain.Status;

import java.time.Instant;

import static java.lang.String.format;

public class ToolkitInstrumented {

    public String sessionId;
    public Instant createdAt;
    public Status status;

    public ToolkitInstrumented(String sessionId, Instant createdAt, Status status) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.status = status;
    }

    @LogEntry(enter = true, exit = true)
    public void mutate(Status status) {
        this.status = status;
    }

    @LogEntry(enter = true)
    public void onlyEnter() {
        System.out.println(format("Logger only in ENTER"));
    }

    @LogEntry(exit = true)
    public void onlyExit() {
        System.out.println(format("Logger only in EXIT"));
    }

    public void notMutate() {
        System.out.println("No logger");
    }

//    public static void main(String[] args) {
//        ToolkitInstrumented toolkitInstrumented = new ToolkitInstrumented("mock.id", Instant.now(), Status.APPROVED);
//        toolkitInstrumented.mutate(Status.PENDING);
//        toolkitInstrumented.notMutate(1);
//    }
}