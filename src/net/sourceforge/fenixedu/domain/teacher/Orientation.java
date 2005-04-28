/*
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class Orientation extends Orientation_Base {
    private OrientationType orientationType;

    /**
     * @return Returns the orientationType.
     */
    public OrientationType getOrientationType() {
        return orientationType;
    }

    /**
     * @param orientationType
     *            The orientationType to set.
     */
    public void setOrientationType(OrientationType orientationType) {
        this.orientationType = orientationType;
    }

}