package bank.bankco.requests;

public class Response {
    private final String feedback;
    private final boolean terminate;

    public Response(String response) {
        this(response, false);
    }

    public Response(String feedback, boolean terminate) {
        this.feedback = feedback;
        this.terminate = terminate;
    }

    public String getFeedback() {
        return feedback;
    }

    public boolean shouldTerminate() {
        return terminate;
    }
}
