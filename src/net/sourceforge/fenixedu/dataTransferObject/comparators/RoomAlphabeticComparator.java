/*
 * Created on 25/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.comparators;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;

/**
 * @author João Mota
 * 
 * 25/Jun/2003 fenix-branch net.sourceforge.fenixedu.dataTransferObject.comparators
 *  
 */
public class RoomAlphabeticComparator implements Comparator {

    /**
     *  
     */
    public RoomAlphabeticComparator() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        InfoRoom room0 = (InfoRoom) arg0;
        InfoRoom room1 = (InfoRoom) arg1;
        return room0.getNome().compareToIgnoreCase(room1.getNome());
    }

}