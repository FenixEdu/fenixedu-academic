/*
 * Created on 16/Jan/2003
 *  
 */
package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Barbosa
 * @author Pica
 */

public class GrantContractEndDateBeforeBeginDateException extends FenixServiceException {

    public GrantContractEndDateBeforeBeginDateException() {
    }

    public GrantContractEndDateBeforeBeginDateException(String message) {
        super(message);
    }

    public GrantContractEndDateBeforeBeginDateException(Throwable cause) {
        super(cause);
    }

    public GrantContractEndDateBeforeBeginDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantContractEndDateBeforeBeginDateException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}