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

public class GrantResponsibleTeacherPeriodNotWithinContractPeriodException extends FenixServiceException {

	public GrantResponsibleTeacherPeriodNotWithinContractPeriodException() {
	}
    
	public GrantResponsibleTeacherPeriodNotWithinContractPeriodException(String message){
		super(message);
	}
    
	public GrantResponsibleTeacherPeriodNotWithinContractPeriodException(Throwable cause) {
		super(cause);
	}

	public GrantResponsibleTeacherPeriodNotWithinContractPeriodException(String message, Throwable cause) {
		super(message, cause);
	}
	public String toString() {
				String result = "[GrantResponsibleTeacherPeriodNotWithinContractPeriodException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}
}