package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.curriculum.GradeType;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.IDelegate;

public class Degree extends Degree_Base {

    protected Degree() {
        super();
    };

    protected Degree(String name, String nameEn) {
        this();
        commonFieldsChange(name, nameEn);
    }

    private void commonFieldsChange(String name, String nameEn) {
        if (name == null) {
            throw new DomainException("degree.name.not.null");
        } else if(nameEn == null) {
            throw new DomainException("degree.name.en.not.null");
        } 

        this.setNome(name);
        this.setNameEn(nameEn);
    }
    
    public Degree(String name, String nameEn, String code, DegreeType degreeType, String concreteClassForDegreeCurricularPlans) {
        this(name, nameEn);
        oldStructureFieldsChange(code, degreeType);
        
        if (concreteClassForDegreeCurricularPlans == null) {
            throw new DomainException("degree.concrete.class.not.null");
        }
        this.setConcreteClassForDegreeCurricularPlans(concreteClassForDegreeCurricularPlans);
        
        new DegreeInfo(this);
    }

    private void oldStructureFieldsChange(String code, DegreeType degreeType) {
        if (code == null) {
            throw new DomainException("degree.code.not.null");
        } else if(degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } 

        this.setSigla(code);
        this.setTipoCurso(degreeType);
    }

    
    public Degree(String namePt, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType, GradeType gradeType) {
        this(namePt, nameEn);
        newStructureFieldsChange(acronym, bolonhaDegreeType, gradeType);
    }
    
    private void newStructureFieldsChange(String acronym, BolonhaDegreeType bolonhaDegreeType, GradeType gradeType) {
        if (acronym == null) {
            throw new DomainException("degree.acronym.not.null");
        } else if(bolonhaDegreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } else if (gradeType == null) {
            throw new DomainException("degree.grade.type.not.null");
        }

        this.setAcronym(acronym);
        this.setBolonhaDegreeType(bolonhaDegreeType);
        this.setGradeType(gradeType);
    }

    public void edit(String name, String nameEn, String code, DegreeType degreeType) {
        commonFieldsChange(name, nameEn);
        oldStructureFieldsChange(code, degreeType);
        
        if(!hasAnyDegreeInfos())  
            new DegreeInfo(this);
    }

    public void edit(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType, GradeType gradeType) {
        commonFieldsChange(name, nameEn);
        newStructureFieldsChange(acronym, bolonhaDegreeType, gradeType);
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
	
}
