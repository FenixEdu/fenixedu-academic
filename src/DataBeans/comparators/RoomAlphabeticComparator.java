/*
 * Created on 25/Jun/2003
 *
 * 
 */
package DataBeans.comparators;

import java.util.Comparator;

import DataBeans.InfoRoom;

/**
 * @author João Mota
 * 
 * 25/Jun/2003 fenix-branch DataBeans.comparators
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