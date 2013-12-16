/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class EvaluationMethod extends EvaluationMethod_Base {

    public EvaluationMethod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void edit(MultiLanguageString evaluationElements) {
        if (evaluationElements == null) {
            throw new NullPointerException();
        }

        setEvaluationElements(evaluationElements);
    }

    public void delete() {
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public void setEvaluationElements(MultiLanguageString evaluationElements) {
        ContentManagementLog.createLog(this.getExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.curricular.evaluation.method", this.getExecutionCourse().getNome(), this
                        .getExecutionCourse().getDegreePresentationString());
        super.setEvaluationElements(evaluationElements);
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEvaluationElements() {
        return getEvaluationElements() != null;
    }

}
