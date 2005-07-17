/*
 * Degree.java
 *
 * Created on 31 de Outubro de 2002, 15:19
 */

package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

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
	
	public void delete() throws DomainException {
		
		if (!hasAnyDegreeCurricularPlans()) {
			
			Iterator oicrIterator = getOldInquiriesCoursesResIterator();
			while (oicrIterator.hasNext()) {
				IOldInquiriesCoursesRes oicr = (IOldInquiriesCoursesRes) oicrIterator.next();
				oicrIterator.remove();
				oicr.removeDegree();
				oicr.delete();
			}
		
			Iterator oitrIterator = getOldInquiriesTeachersResIterator();
			while (oitrIterator.hasNext()) {
				IOldInquiriesTeachersRes oitr = (IOldInquiriesTeachersRes) oitrIterator.next();
				oitrIterator.remove();
				oitr.removeDegree();
				oitr.delete();
			}
				
			Iterator oisIterator = getOldInquiriesSummaryIterator();
			while (oisIterator.hasNext()) {
				IOldInquiriesSummary ois = (IOldInquiriesSummary) oisIterator.next();
				oisIterator.remove();
				ois.removeDegree();
				ois.delete();
			}
			
			Iterator delegatesIterator = getDelegateIterator();
			while(delegatesIterator.hasNext()) {
				IDelegate delegate = (IDelegate)delegatesIterator.next();
				delegatesIterator.remove();
				delegate.removeDegree();
				delegate.delete();
			}
			
			for (IDegreeInfo degreeInfo : getDegreeInfos()) {
				((DegreeInfo)degreeInfo).deleteDomainObject();
			}
			
			deleteDomainObject();
		} else {
			throw new DomainException(this.getClass().getName(),"");
		}
	}
}
