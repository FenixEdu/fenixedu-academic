package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ActiveStudentCurricularPlanAlreadyExistsServiceException extends FenixServiceException {

    public ActiveStudentCurricularPlanAlreadyExistsServiceException() {
    }

    public ActiveStudentCurricularPlanAlreadyExistsServiceException(Throwable cause) {
        super(cause);
    }

    public ActiveStudentCurricularPlanAlreadyExistsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[ActiveStudentCurricularPlanAlreadyExistsServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}