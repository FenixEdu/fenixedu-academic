package ServidorAplicacao.Servico.exceptions;

/**
 * @author David Santos Jan 29, 2004
 */

public class ChosenAreasAreIncompatibleServiceException extends FenixServiceException
{

	public ChosenAreasAreIncompatibleServiceException()
	{
	}

	public ChosenAreasAreIncompatibleServiceException(String message)
	{
		super(message);
	}

	public ChosenAreasAreIncompatibleServiceException(Throwable cause)
	{
		super(cause);
	}

	public ChosenAreasAreIncompatibleServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public String toString()
	{
		String result = "[ChosenAreasAreIncompatibleServiceException\n";
		result += "message" + this.getMessage() + "\n";
		result += "cause" + this.getCause() + "\n";
		result += "]";
		return result;
	}

}