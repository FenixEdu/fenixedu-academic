/*
 * Created on 28/Nov/2003
 * 
 */
 
package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantResponsibleTeacherPeriodConflictException extends FenixServiceException {

	public GrantResponsibleTeacherPeriodConflictException() {
	}
    
	public GrantResponsibleTeacherPeriodConflictException(String message){
		super(message);
	}
    
	public GrantResponsibleTeacherPeriodConflictException(Throwable cause) {
		super(cause);
	}

	public GrantResponsibleTeacherPeriodConflictException(String message, Throwable cause) {
		super(message, cause);
	}
	public String toString() {
				String result = "[GrantResponsibleTeacherPeriodConflictException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}
}