/*
 * ExistingActionException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */
 
package ServidorApresentacao.Action.exceptions;

/**
 *
 * @author  Luis Cruz & Nuno Nunes & João Mota
 */

public class NonExistingActionException extends FenixActionException {

	public static String key = "error.exception.nonExisting";
	
	public NonExistingActionException(String value) {
			super(key, value);
		}

	public NonExistingActionException(Throwable cause) {
		super(key, cause);
	}
	
	public NonExistingActionException(Object value, Throwable cause) {
		super(key, value, cause);
	}	
	
	public NonExistingActionException(Object[] values, Throwable cause) {
		super(key, values, cause);
	}
    

	/**
	 * @return String
	 */
	public static String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * @param key The key to set
	 */
	public static void setKey(String key) {
		NonExistingActionException.key = key;
	}
	
	public String toString() {
			String result = "[NonExistingActionException\n";
			result += "property" +this.getProperty()+ "\n";
			result += "error" +this.getError()+ "\n";
			result += "cause" +this.getCause()+ "\n";
			result += "]";
			return result;
		}
//TODO find a way of internationalizing the message passed as argument to the exception error message of the resource bundle
}