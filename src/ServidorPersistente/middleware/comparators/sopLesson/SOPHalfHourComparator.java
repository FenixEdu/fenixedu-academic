/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.comparators.sopLesson;

import java.util.Comparator;

import Dominio.SOPAulas;

/**
 * @author jpvl
 */
public class SOPHalfHourComparator implements Comparator {

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        SOPAulas halfHour1 = getSopAulas(o1);
        SOPAulas halfHour2 = getSopAulas(o2);
        return halfHour1.getHora() - halfHour2.getHora();
    }

    private SOPAulas getSopAulas(Object o) {
        if (o instanceof SOPAulas) {
            return (SOPAulas) o;
        }
        if (o != null) {
            throw new IllegalArgumentException("It was expected: "
                    + SOPAulas.class.getName() + " but received "
                    + o.getClass().getName());
        }

        throw new IllegalArgumentException("Received null object");
    }

}