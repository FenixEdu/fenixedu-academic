package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NoActiveStudentCurricularPlanServiceException extends FenixServiceException {

    public NoActiveStudentCurricularPlanServiceException() {
    }

    public NoActiveStudentCurricularPlanServiceException(Throwable cause) {
        super(cause);
    }

    public NoActiveStudentCurricularPlanServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NoActiveStudentCurricularPlanServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}