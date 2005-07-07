package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.fileSuport.INode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExecutionCourse extends ExecutionCourse_Base {

    public String toString() {
        String result = "[EXECUTION_COURSE";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", theoreticalHours=" + getTheoreticalHours();
        result += ", praticalHours=" + getPraticalHours();
        result += ", theoPratHours=" + getTheoPratHours();
        result += ", labHours=" + getLabHours();
        result += ", executionPeriod=" + getExecutionPeriod();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EC" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionPeriod executionPeriod = getExecutionPeriod();
        return executionPeriod;
    }

    public List getGroupProperties() {
        List groupProperties = new ArrayList();
        if (getGroupPropertiesExecutionCourse() != null) {
            Iterator iterGroupPropertiesExecutionCourse = getGroupPropertiesExecutionCourse().iterator();
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = null;
            while (iterGroupPropertiesExecutionCourse.hasNext()) {
                groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) iterGroupPropertiesExecutionCourse
                        .next();
                if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1
                        || groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                    groupProperties.add(groupPropertiesExecutionCourse.getGroupProperties());
                }
            }
        }
        return groupProperties;
    }

    public IGroupProperties getGroupPropertiesByName(String name) {
        Iterator iter = getGroupProperties().iterator();
        while (iter.hasNext()) {
            IGroupProperties gp = (IGroupProperties) iter.next();
            if ((gp.getName()).equals(name))
                return gp;
        }
        return null;

    }

    public boolean existsGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        return getGroupPropertiesExecutionCourse().contains(groupPropertiesExecutionCourse);
    }

    public boolean existsGroupPropertiesExecutionCourse() {
        return getGroupPropertiesExecutionCourse().isEmpty();
    }

    public boolean hasProposals() {
        boolean result = false;
        boolean found = false;
        List groupPropertiesExecutionCourseList = getGroupPropertiesExecutionCourse();
        Iterator iter = groupPropertiesExecutionCourseList.iterator();
        while (iter.hasNext() && !found) {

            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourseAux = (IGroupPropertiesExecutionCourse) iter
                    .next();
            if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 3) {
                result = true;
                found = true;
            }

        }
        return result;
    }

    public boolean isMasterDegreeOnly() {
        return CollectionUtils.exists(this.getAssociatedCurricularCourses(), new Predicate() {

            public boolean evaluate(Object input) {
                ICurricularCourse curricularCourse = (ICurricularCourse) input;
                return !curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                        DegreeType.DEGREE);
            }
        });
    }

    public void createSite() {
        final ISite site = new Site();
        site.setExecutionCourse(this);
    }

    public void createEvaluationMethod(final String evaluationElements,
            final String evaluationElementsEng) {
        if (evaluationElements == null || evaluationElementsEng == null)
            throw new NullPointerException();

        final IEvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setEvaluationElements(evaluationElements);
        evaluationMethod.setEvaluationElementsEn(evaluationElementsEng);
        evaluationMethod.setExecutionCourse(this);
    }

    public void createBibliographicReference(final String title, final String authors,
            final String reference, final String year, final Boolean optional) {
        if (title == null || authors == null || reference == null || year == null || optional == null)
            throw new NullPointerException();

        final IBibliographicReference bibliographicReference = new BibliographicReference();
        bibliographicReference.setTitle(title);
        bibliographicReference.setAuthors(authors);
        bibliographicReference.setReference(reference);
        bibliographicReference.setYear(year);
        bibliographicReference.setOptional(optional);
        bibliographicReference.setExecutionCourse(this);
    }

    public void createCourseReport() {

    }
}
