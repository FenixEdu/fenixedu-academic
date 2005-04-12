/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class Delegate extends Delegate_Base {
    private DelegateYearType yearType;

    /**
     * @return Returns the type.
     */
    public DelegateYearType getYearType() {
        return yearType;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setYearType(DelegateYearType yearType) {
        this.yearType = yearType;
    }

}