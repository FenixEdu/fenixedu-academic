package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidStudentNumberServiceException extends FenixServiceException {

    public InvalidStudentNumberServiceException() {
    }

    public InvalidStudentNumberServiceException(Throwable cause) {
        super(cause);
    }

    public InvalidStudentNumberServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[InvalidStudentNumberServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}