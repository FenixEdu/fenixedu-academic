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

	public String toString() {
			String result = "[FenixServiceException\n";
			result += "message" +this.getMessage()+ "\n";
			result += "cause" +this.getCause()+ "\n";
			result += "]";
			return result;
		}

}

/* Created by Nuno Antão */