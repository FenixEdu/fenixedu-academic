/*
 * Created on 6/Mar/2003
 *
 * 
 */
package ServidorPersistente.exceptions;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author João Mota
 */
public class notAuthorizedPersistentDeleteException extends ExcepcaoPersistencia {

    /**
     *  
     */
    public notAuthorizedPersistentDeleteException() {
        super();
    }

    /**
     * @param erro
     * @param ex
     */
    public notAuthorizedPersistentDeleteException(int erro, Exception ex) {
        super(erro, ex);
    }

    /**
     * @param error
     * @param ex
     */
    public notAuthorizedPersistentDeleteException(String error, Exception ex) {
        super(error, ex);
    }

    /**
     * @param erro
     */
    public notAuthorizedPersistentDeleteException(int erro) {
        super(erro);

    }

    /**
     * @param msg
     */
    public notAuthorizedPersistentDeleteException(String msg) {
        super(msg);

    }

    public String toString() {
        String result = "[notAuthorizedPersistentDeleteException\n";
        result += "message" + this.getMessage() + "\n";
        result += "error" + this.getErrorKey() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}