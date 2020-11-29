package pcmember.exception;

public class CannotAssignArticlesException extends RuntimeException {
    private static final long serialVersionUID = -1762533940710869977L;

    public CannotAssignArticlesException() {
        super("We can not assign the articles under the rules, please add some new PCMembers.");
    }
}
