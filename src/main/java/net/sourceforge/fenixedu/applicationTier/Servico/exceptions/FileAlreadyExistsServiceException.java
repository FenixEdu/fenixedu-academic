package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author João Mota
 */
public class FileAlreadyExistsServiceException extends FenixServiceException {

    public FileAlreadyExistsServiceException() {
    }

    public FileAlreadyExistsServiceException(Throwable cause) {
        super(cause);
    }

    public FileAlreadyExistsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String result = "[FileAlreadyExistsServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}