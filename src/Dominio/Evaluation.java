package Dominio;

import java.util.List;

/**
 * @author Tânia Pousão 24 de Junho de 2003
 */
public class Evaluation extends DomainObject implements IEvaluation {
    private String ojbConcreteClass;

    private String publishmentMessage;

    private List associatedExecutionCourses;

    public Evaluation() {
        super();
        this.ojbConcreteClass = this.getClass().getName();
    }

    public Evaluation(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return String
     */
    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    /**
     * @param string
     */
    public void setOjbConcreteClass(String string) {
        ojbConcreteClass = string;
    }

    /**
     * @return String
     */
    public String getPublishmentMessage() {
        return publishmentMessage;
    }

    /**
     * @param string
     */
    public void setPublishmentMessage(String string) {
        publishmentMessage = string;
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