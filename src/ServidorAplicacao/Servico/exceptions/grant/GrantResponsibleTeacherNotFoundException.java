/*
 * Created on 19/Nov/2003
 * 
 */
 
package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantResponsibleTeacherNotFoundException extends FenixServiceException {

	public GrantResponsibleTeacherNotFoundException() {
	}
    
	public GrantResponsibleTeacherNotFoundException(String message){
		super(message);
	}
    
	public GrantResponsibleTeacherNotFoundException(Throwable cause) {
		super(cause);
	}

	public GrantResponsibleTeacherNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	public String toString() {
				String result = "[GrantResponsibleTeacherNotFoundException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}
}