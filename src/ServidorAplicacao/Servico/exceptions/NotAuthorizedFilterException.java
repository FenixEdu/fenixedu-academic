/*
 * Created on Dec 16, 2003
 *  
 */
package ServidorAplicacao.Servico.exceptions;

import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class NotAuthorizedFilterException extends FilterException
{
    /**
	 *  
	 */
    public NotAuthorizedFilterException()
    {
        super();
    }

    /**
	 * @param message
	 */
    public NotAuthorizedFilterException(String message)
    {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public NotAuthorizedFilterException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
	 * @param cause
	 */
    public NotAuthorizedFilterException(Throwable cause)
    {
        super(cause);
    }

}
