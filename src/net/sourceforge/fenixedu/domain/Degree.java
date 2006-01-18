package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.Delegate;

public class Degree extends Degree_Base {

    protected Degree() {
        super();
    };

    public Degree(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale,
            String concreteClassForDegreeCurricularPlans) {
        this();
        commonFieldsChange(name, nameEn, gradeScale);
        oldStructureFieldsChange(code, degreeType);

        if (concreteClassForDegreeCurricularPlans == null) {
            throw new DomainException("degree.concrete.class.not.null");
        }
        this.setConcreteClassForDegreeCurricularPlans(concreteClassForDegreeCurricularPlans);

        new DegreeInfo(this);
    }

    public Degree(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType,
            Double ectsCredits, GradeScale gradeScale) {
        this();
        commonFieldsChange(name, nameEn, gradeScale);
        newStructureFieldsChange(acronym, bolonhaDegreeType, ectsCredits);
    }

    private void commonFieldsChange(String name, String nameEn, GradeScale gradeScale) {
        if (name == null) {
            throw new DomainException("degree.name.not.null");
        } else if(nameEn == null) {
            throw new DomainException("degree.name.en.not.null");
        } 

        this.setNome(name);
        this.setNameEn(nameEn);
        this.setGradeScale(gradeScale);
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
    
    private void newStructureFieldsChange(String acronym, BolonhaDegreeType bolonhaDegreeType, Double ectsCredits) {
        if (acronym == null) {
            throw new DomainException("degree.acronym.not.null");
        } else if(bolonhaDegreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } else if (ectsCredits == null) {
            throw new DomainException("degree.ectsCredits.not.null");
        }

        this.setAcronym(acronym);
        this.setBolonhaDegreeType(bolonhaDegreeType);
        this.setEctsCredits(ectsCredits);
    }

    public void edit(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale) {
        commonFieldsChange(name, nameEn, gradeScale);
        oldStructureFieldsChange(code, degreeType);
        
        if(!hasAnyDegreeInfos())  
            new DegreeInfo(this);
    }

    public void edit(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType,
            Double ectsCredits, GradeScale gradeScale) {
        checkIfCanEdit(bolonhaDegreeType);
        commonFieldsChange(name, nameEn, gradeScale);
        newStructureFieldsChange(acronym, bolonhaDegreeType, ectsCredits);
    }
    
    private void checkIfCanEdit(final BolonhaDegreeType bolonhaDegreeType) {
        if (! this.getBolonhaDegreeType().equals(bolonhaDegreeType) && hasAnyDegreeCurricularPlans()) {
            throw new DomainException("degree.cant.edit.bolonhaDegreeType");
        }
    }

    public Boolean getCanBeDeleted() {
        return !hasAnyDegreeCurricularPlans();
    }
    
    public void delete() throws DomainException {
        
        if (getCanBeDeleted()) {
            
            Iterator oicrIterator = getAssociatedOldInquiriesCoursesResIterator();
            while (oicrIterator.hasNext()) {
                OldInquiriesCoursesRes oicr = (OldInquiriesCoursesRes) oicrIterator.next();
                oicrIterator.remove();
                oicr.removeDegree();
                oicr.delete();
            }
        
            Iterator oitrIterator = getAssociatedOldInquiriesTeachersResIterator();
            while (oitrIterator.hasNext()) {
                OldInquiriesTeachersRes oitr = (OldInquiriesTeachersRes) oitrIterator.next();
                oitrIterator.remove();
                oitr.removeDegree();
                oitr.delete();
            }
                
            Iterator oisIterator = getAssociatedOldInquiriesSummariesIterator();
            while (oisIterator.hasNext()) {
                OldInquiriesSummary ois = (OldInquiriesSummary) oisIterator.next();
                oisIterator.remove();
                ois.removeDegree();
                ois.delete();
            }
            
            Iterator delegatesIterator = getDelegateIterator();
            while(delegatesIterator.hasNext()) {
                Delegate delegate = (Delegate)delegatesIterator.next();
                delegatesIterator.remove();
                delegate.removeDegree();
                delegate.delete();
            }
            
            Iterator degreeInfosIterator = getDegreeInfosIterator();
            while (degreeInfosIterator.hasNext()) {
                DegreeInfo degreeInfo = (DegreeInfo) degreeInfosIterator.next();
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

    public DegreeCurricularPlan getNewDegreeCurricularPlan() {
        DegreeCurricularPlan degreeCurricularPlan = null;

        try {
            Class classDefinition = Class.forName(getConcreteClassForDegreeCurricularPlans());
            degreeCurricularPlan = (DegreeCurricularPlan) classDefinition.newInstance();
        } catch (InstantiationException e) {
        	throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
        	throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException(e);
        }

        return degreeCurricularPlan;
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getTipoCurso().getGradeScale();
    }

}
