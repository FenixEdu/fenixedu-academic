/*
 * Created on Jun 23, 2004
 *
 */
package Util;

/**
 * @author Pica
 * @author Barbosa
 */
public class NameUtils extends FenixUtil {

    /**
     * returns the person last name
     */
    public static String getLastName(String name) {
        if (name != null) {
            String tempNome = name.trim();
            if (tempNome.lastIndexOf(" ") == -1) //There is no space in the
            // name
            {
                if (tempNome.length() == 0) //The name is empty
                {
                    return "";
                }

                return tempNome;

            }

            return tempNome.substring(tempNome.lastIndexOf(" "), tempNome.length());

        }
        return "";
    }

    /**
     * @return the person first names (the whole name except it's last
     */
    public static String getFirstName(String name) {
        if (name != null) {
            String tempNome = name.trim();
            if (tempNome.lastIndexOf(" ") == -1) //There is no space in the
            // name
            {
                if (tempNome.length() == 0) //The name is empty
                {
                    return "";
                }

                return tempNome;

            }

            return tempNome.substring(0, tempNome.lastIndexOf(" "));

        }
        return "";
    }

}