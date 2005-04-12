package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author Tânia Pousão 24 de Junho de 2003
 */
public class Evaluation extends Evaluation_Base {
    private List associatedExecutionCourses;

    public Evaluation() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public Evaluation(Integer idInternal) {
        setIdInternal(idInternal);
    }


    /**
     * @return
     */
    public List getAssociatedExecutionCourses() {
        return associatedExecutionCourses;
    }

    /**
     * @param list
     */
    public void setAssociatedExecutionCourses(List list) {
        associatedExecutionCourses = list;
    }
}