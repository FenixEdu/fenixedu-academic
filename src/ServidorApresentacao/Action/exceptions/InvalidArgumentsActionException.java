/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.exceptions;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InvalidArgumentsActionException extends FenixActionException{

	public static String key = "error.exception.nonExisting";
	/**
	 * 
	 */
	public InvalidArgumentsActionException(Throwable cause) {
		super(key, cause);
	}
	
	public InvalidArgumentsActionException(Object value, Throwable cause) {
		super(key, value, cause);
	}	
	
	public InvalidArgumentsActionException(Object[] values, Throwable cause) {
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
		ExistingActionException.key = key;
	}
	
	public String toString() {
			String result = "[InvalidArgumentsException\n";
			result += "property" +this.getProperty()+ "\n";
			result += "error" +this.getError()+ "\n";
			result += "cause" +this.getCause()+ "\n";
			result += "]";
			return result;
		}

}
