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

public class GrantResponsibleTeacherEndDateBeforeBeginDateException extends FenixServiceException {

	public GrantResponsibleTeacherEndDateBeforeBeginDateException() {
	}
    
	public GrantResponsibleTeacherEndDateBeforeBeginDateException(String message){
		super(message);
	}
    
	public GrantResponsibleTeacherEndDateBeforeBeginDateException(Throwable cause) {
		super(cause);
	}

	public GrantResponsibleTeacherEndDateBeforeBeginDateException(String message, Throwable cause) {
		super(message, cause);
	}
	public String toString() {
				String result = "[GrantResponsibleTeacherEndDateBeforeBeginDateException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}
}