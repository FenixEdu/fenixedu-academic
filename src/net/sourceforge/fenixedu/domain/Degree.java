package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.util.MarkType;

public class Degree extends Degree_Base {

    public Degree() {
        super();
    };

    public Degree(String name, String nameEn, String sigla) {
        this();
        setNome(name);
        setNameEn(nameEn);
        setSigla(sigla);
        
        new DegreeInfo(this);
    }

    public Degree(String name, String nameEn, String sigla, DegreeType degreeType, String concreteClassForDegreeCurricularPlans) {
        this(name, nameEn, sigla);
        setTipoCurso(degreeType);
        setConcreteClassForDegreeCurricularPlans(concreteClassForDegreeCurricularPlans);
    }
    
    public Degree(String namePt, String nameEn, String code, BolonhaDegreeType bolonhaDegreeType, MarkType markType) {
        this(namePt, nameEn, code);
        this.setBolonhaDegreeType(bolonhaDegreeType);
        this.setMarkType(markType);
    }
    
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
	
	
	public void edit(String name, String nameEn, String sigla, DegreeType degreeType) {
		setNome(name);
		setNameEn(nameEn);
		setSigla(sigla);
		setTipoCurso(degreeType);
		
        if(!hasAnyDegreeInfos())  
			new DegreeInfo(this);
	}
		
	
	public void delete() throws DomainException {
		
		if (!hasAnyDegreeCurricularPlans()) {
			
			Iterator oicrIterator = getAssociatedOldInquiriesCoursesResIterator();
			while (oicrIterator.hasNext()) {
				IOldInquiriesCoursesRes oicr = (IOldInquiriesCoursesRes) oicrIterator.next();
				oicrIterator.remove();
				oicr.removeDegree();
				oicr.delete();
			}
		
			Iterator oitrIterator = getAssociatedOldInquiriesTeachersResIterator();
			while (oitrIterator.hasNext()) {
				IOldInquiriesTeachersRes oitr = (IOldInquiriesTeachersRes) oitrIterator.next();
				oitrIterator.remove();
				oitr.removeDegree();
				oitr.delete();
			}
				
			Iterator oisIterator = getAssociatedOldInquiriesSummariesIterator();
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
			
			Iterator degreeInfosIterator = getDegreeInfosIterator();
			while (degreeInfosIterator.hasNext()) {
				IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfosIterator.next();
				degreeInfosIterator.remove();
				degreeInfo.removeDegree();
				degreeInfo.delete();
			}
			
			deleteDomainObject();
		} else {
			throw new DomainException("error.degree.has.degree.curricular.plans");
		}
	}
}
