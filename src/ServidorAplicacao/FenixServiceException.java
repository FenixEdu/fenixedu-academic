package ServidorAplicacao;

public class FenixServiceException extends Exception {

    public FenixServiceException() {
    }
    
    public FenixServiceException(String s) {
        super(s);
    }
    
	public FenixServiceException(Throwable cause) {
		super(cause);
	}    

	public FenixServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}

/* Created by Nuno Antão */