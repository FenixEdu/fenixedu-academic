package ServidorAplicacao.Servico.exceptions;

/**
 * @author David Santos Jan 29, 2004
 */

public class BothAreasAreTheSameServiceException extends FenixServiceException
{

	public BothAreasAreTheSameServiceException()
	{
	}

	public BothAreasAreTheSameServiceException(String message)
	{
		super(message);
	}

	public BothAreasAreTheSameServiceException(Throwable cause)
	{
		super(cause);
	}

	public BothAreasAreTheSameServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public String toString()
	{
		String result = "[BothAreasAreTheSameServiceException\n";
		result += "message" + this.getMessage() + "\n";
		result += "cause" + this.getCause() + "\n";
		result += "]";
		return result;
	}

}