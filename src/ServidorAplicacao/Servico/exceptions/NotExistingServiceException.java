/*
 * Created on Jun 11, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.exceptions;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class NotExistingServiceException extends FenixServiceException {

	/**
	 * 
	 */
	public NotExistingServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errorType
	 */
	public NotExistingServiceException(int errorType) {
		super(errorType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 */
	public NotExistingServiceException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NotExistingServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotExistingServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		String result = "[NotExistingServiceException\n";
		result += "message" +this.getMessage()+ "\n";
		result += "cause" +this.getCause()+ "\n";
		result += "]";
		return result;
	}

}
