package Util.classProperties;

import Util.FenixUtil;

/**
 * @author David Santos in Apr 6, 2004
 */

public class GeneralClassPropertyName extends FenixUtil {
    protected String name;

    public GeneralClassPropertyName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}