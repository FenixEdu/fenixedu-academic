/*
 * Created on 17/Nov/2003
 *
 * 
 */
package ServidorAplicacao.Servico.exceptions.guide;

import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 */
public class InvalidReimbursementValueServiceException extends InvalidArgumentsServiceException
{

    /**
     * 
     */
    public InvalidReimbursementValueServiceException()
    {

    }

    /**
     * @param s
     */
    public InvalidReimbursementValueServiceException(String s)
    {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidReimbursementValueServiceException(Throwable cause)
    {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidReimbursementValueServiceException(String message, Throwable cause)
    {
        super(message, cause);

    }
    public String toString()
    {
        String result = "[InvalidReimbursementValueServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}
