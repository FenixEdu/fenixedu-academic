package ServidorAplicacao.Servico.exceptions;

public class FenixServiceException extends Exception {
	private int errorType;
    
	/**
	 * @return
	 */
	public int getErrorType() {
		return this.errorType;
	}

    public FenixServiceException() {
    }

	public FenixServiceException(int errorType) {
		this.errorType = errorType;
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
			String result = "[" +  this.getClass().getName() +"\n";
			result += "message " +this.getMessage()+ "\n";
			result += "cause " +this.getCause()+ "\n";
			result += "]";
			return result;
		}

}

/* Created by Nuno Antão */