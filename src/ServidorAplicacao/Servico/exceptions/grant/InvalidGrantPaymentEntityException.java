/*
 * Created on 03/Feb/2004
 *  
 */
package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Barbosa
 * @author Pica
 */

public class InvalidGrantPaymentEntityException extends FenixServiceException
{

    public InvalidGrantPaymentEntityException()
    {
    }

    public InvalidGrantPaymentEntityException(String message)
    {
        super(message);
    }

    public InvalidGrantPaymentEntityException(Throwable cause)
    {
        super(cause);
    }

    public InvalidGrantPaymentEntityException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public String toString()
    {
        String result = "[InvalidGrantPaymentEntityException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}