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
public class InvalidReimbursementValueSumServiceException extends InvalidArgumentsServiceException
{

    /**
     * 
     */
    public InvalidReimbursementValueSumServiceException()
    {

    }

    /**
     * @param s
     */
    public InvalidReimbursementValueSumServiceException(String s)
    {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidReimbursementValueSumServiceException(Throwable cause)
    {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidReimbursementValueSumServiceException(String message, Throwable cause)
    {
        super(message, cause);

    }
    public String toString()
    {
        String result = "[InvalidReimbursementValueSumServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}
