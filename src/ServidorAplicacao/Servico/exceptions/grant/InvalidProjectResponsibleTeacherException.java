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

public class InvalidProjectResponsibleTeacherException extends FenixServiceException
{

    public InvalidProjectResponsibleTeacherException()
    {
    }

    public InvalidProjectResponsibleTeacherException(String message)
    {
        super(message);
    }

    public InvalidProjectResponsibleTeacherException(Throwable cause)
    {
        super(cause);
    }

    public InvalidProjectResponsibleTeacherException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public String toString()
    {
        String result = "[InvalidProjectResponsibleTeacherException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}