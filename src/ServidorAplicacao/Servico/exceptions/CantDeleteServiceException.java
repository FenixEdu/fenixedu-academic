/*
 * Created on 19/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.exceptions;

import ServidorAplicacao.FenixServiceException;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CantDeleteServiceException extends FenixServiceException {

	public CantDeleteServiceException() {
	}
    
	public CantDeleteServiceException(Throwable cause) {
		super(cause);
	}

	public CantDeleteServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	public String toString() {
				String result = "[CantDeleteServiceException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}


}