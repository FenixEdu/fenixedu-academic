/*
 * Degree.java
 *
 * Created on 31 de Outubro de 2002, 15:19
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.IDelegate;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

public class Degree extends Degree_Base {

    public String toString() {
        String result = "[CURSO";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getSigla();
        result += ", nome=" + getNome();
        result += ", tipoCurso=" + getTipoCurso();
        result += "]";
        return result;
    }

    public IDegreeCurricularPlan getNewDegreeCurricularPlan() {
        IDegreeCurricularPlan degreeCurricularPlan = null;

        try {
            Class classDefinition = Class.forName(getConcreteClassForDegreeCurricularPlans());
            degreeCurricularPlan = (IDegreeCurricularPlan) classDefinition.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        }

        return degreeCurricularPlan;
    }
	
	public void deleteDegree () throws DomainException {
		
		if (getDegreeCurricularPlans() == null || getDegreeCurricularPlans().isEmpty()) {
			
			for (IOldInquiriesCoursesRes oldICR : getOldInquiriesCoursesRes()) {
				oldICR.setExecutionPeriod(null);
			}
			getOldInquiriesCoursesRes().clear();
			
			for (IOldInquiriesTeachersRes oldITR : getOldInquiriesTeachersRes()) {
				oldITR.setExecutionPeriod(null);
				oldITR.setTeacher(null);
			}
			getOldInquiriesTeachersRes().clear();
			
			for (IOldInquiriesSummary oldIS : getOldInquiriesSummary()) {
				oldIS.setExecutionPeriod(null);
			}
			getOldInquiriesSummary().clear();
			
			for (IDelegate delegate : getDelegate()) {
				delegate.setExecutionYear(null);
				delegate.setStudent(null);
			}
			getDelegate().clear();
			
		} else {
			throw new DomainException(this.getClass().getName(),"");
		}
	}
}
