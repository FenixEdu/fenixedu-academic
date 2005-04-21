package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.fileSuport.INode;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ExecutionCourse extends ExecutionCourse_Base {
    
	private List associatedExams = null;
	
	private List associatedEvaluations = null;

    public ExecutionCourse() {
    }

    public ExecutionCourse(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public ExecutionCourse(String nome, String sigla, Double theoreticalHours, Double praticalHours,
            Double theoPratHours, Double labHours, IExecutionPeriod executionPeriod) {

        setNome(nome);
        setSigla(sigla);
        setTheoreticalHours(theoreticalHours);
        setPraticalHours(praticalHours);
        setTheoPratHours(theoPratHours);
        setLabHours(labHours);
        setExecutionPeriod(executionPeriod);
        setComment(new String());
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IExecutionCourse) {
            IExecutionCourse de = (IExecutionCourse) obj;

            resultado = (getSigla().equals(de.getSigla()))
                    && (getExecutionPeriod().equals(de.getExecutionPeriod()));
        }
        return resultado;
    }

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
    
    /**
	 * @return
	 */
	public List getAssociatedEvaluations() {
		return associatedEvaluations;
	}

	/**
     * @param list
     */
    public void setAssociatedEvaluations(List list) {
        associatedEvaluations = list;
    }
    
    /**
	 * @return
	 */
	public List getAssociatedExams() {
		return associatedExams;
	}

	/**
	 * @param list
	 */
	public void setAssociatedExams(List list) {
		associatedExams = list;
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

    public void addGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        if (getGroupPropertiesExecutionCourse() == null) {
            setGroupPropertiesExecutionCourse(new ArrayList());
            getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);
        } else {
            getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);
        }
    }

    public void removeGroupPropertiesExecutionCourse(
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        getGroupPropertiesExecutionCourse().remove(groupPropertiesExecutionCourse);
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
                        TipoCurso.LICENCIATURA_OBJ);
            }
        });
    }
}