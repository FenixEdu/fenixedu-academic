/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Ricardo Rodrigues
 *
 */
public class PublicationArea extends FenixValuedEnum {

	public static final int DIDATIC_TYPE = 1;

    public static final int CIENTIFIC_TYPE = 2;    

    public static final PublicationArea DIDATIC = new PublicationArea("Didatic",
    		PublicationArea.DIDATIC_TYPE);

    public static final PublicationArea CIENTIFIC = new PublicationArea("Cientific",
    		PublicationArea.CIENTIFIC_TYPE);
    
    private PublicationArea(String name, int value) {
        super(name, value);
    }

    public static PublicationArea getEnum(String type) {
        return (PublicationArea) getEnum(PublicationArea.class, type);
    }

    public static PublicationArea getEnum(int type) {
        return (PublicationArea) getEnum(PublicationArea.class, type);
    }

    public static Map getEnumMap() {
        return getEnumMap(PublicationArea.class);
    }

    public static List getEnumList() {
        return getEnumList(PublicationArea.class);
    }

    public static Iterator iterator() {
        return iterator(PublicationArea.class);
    }

    public String toString() {
        return this.getName();
    }

}
