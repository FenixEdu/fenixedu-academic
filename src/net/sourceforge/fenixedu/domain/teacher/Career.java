/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class Career extends Career_Base {

    public Career() {
        this.setOjbConcreteClass(this.getClass().getName());
    }

    public Career(Integer idInternal) {
        setIdInternal(idInternal);
        this.setOjbConcreteClass(this.getClass().getName());
    }

}