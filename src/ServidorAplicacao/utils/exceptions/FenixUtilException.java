/*
 * Created on 2/Jun/2004
 *  
 */
package ServidorAplicacao.utils.exceptions;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class FenixUtilException extends Exception
{
	private int errorType;

	/**
	 *  
	 */
	public FenixUtilException()
	{
		super();
	}

	/**
	 * @param arg0
	 */
	public FenixUtilException(String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public FenixUtilException(int errorType)
	{
		this.errorType = errorType;
	}

	/**
	 * @param arg0
	 */
	public FenixUtilException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public FenixUtilException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public int getErrorType()
	{
		return this.errorType;
	}

	public String toString()
	{
		String result = "[" + this.getClass().getName() + "\n";
		result += "message " + this.getMessage() + "\n";
		result += "cause " + this.getCause() + "\n";
		result += "]";
		return result;
	}

}
