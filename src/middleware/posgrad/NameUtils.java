
package middleware.posgrad;

import java.util.StringTokenizer;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NameUtils {


	public static String removePontuation(String name){
		String result = new String();
		for (int i = 0; i< name.length(); i++){
			if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Á")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("À")) ||
				(String.valueOf(name.charAt(i)).equalsIgnoreCase("Ã"))){
				result += "A";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Ó")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ò")) ||
				(String.valueOf(name.charAt(i)).equalsIgnoreCase("Õ"))){
				result += "O";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("É")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("È"))){
				result += "E";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Í")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ì"))){
				result += "I";
			} else if ((String.valueOf(name.charAt(i)).equalsIgnoreCase("Ú")) || (String.valueOf(name.charAt(i)).equalsIgnoreCase("Ù"))){
				result += "U";
			} else {
				result += name.charAt(i);
			}
		}
		return result;
		
	}
	
	public static String generateCode(String name, int numOfChars){
		String result = new String();
		
		String aux = null;

		// Fix the String
		name = removePontuation(name);

		StringTokenizer stringTokenizer = new StringTokenizer(name, " ");
				
		while(stringTokenizer.hasMoreTokens()){
			aux = stringTokenizer.nextToken();
			if ((aux.charAt(0) != '(') && (aux.charAt(0) != '-') && (aux.charAt(0) != '\'') && 
				(aux.charAt(0) != '´') && (aux.charAt(0) != '`')){
					if (aux.length() <= numOfChars)
						result += aux;
					else
						result += String.valueOf(aux.substring(0, numOfChars));
			}
		}
		return result;
	}
}
