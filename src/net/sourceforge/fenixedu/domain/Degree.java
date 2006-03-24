package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.student.Delegate;

public class Degree extends Degree_Base {

    protected Degree() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

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
            Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea) {
        this();
        commonFieldsChange(name, nameEn, gradeScale);
        newStructureFieldsChange(acronym, bolonhaDegreeType, ectsCredits, prevailingScientificArea);
    }

    private void commonFieldsChange(String name, String nameEn, GradeScale gradeScale) {
        if (name == null) {
            throw new DomainException("degree.name.not.null");
        } else if(nameEn == null) {
            throw new DomainException("degree.name.en.not.null");
        } 

        this.setNome(name.trim());
        this.setNameEn(nameEn.trim());
        this.setGradeScale(gradeScale);
    }
    
    private void oldStructureFieldsChange(String code, DegreeType degreeType) {
        if (code == null) {
            throw new DomainException("degree.code.not.null");
        } else if(degreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } 

        this.setSigla(code.trim());
        this.setTipoCurso(degreeType);
    }
    
    private void newStructureFieldsChange(String acronym, BolonhaDegreeType bolonhaDegreeType, Double ectsCredits, String prevailingScientificArea) {
        if (acronym == null) {
            throw new DomainException("degree.acronym.not.null");
        } else if(bolonhaDegreeType == null) {
            throw new DomainException("degree.degree.type.not.null");
        } else if (ectsCredits == null) {
            throw new DomainException("degree.ectsCredits.not.null");
        }

        this.setAcronym(acronym.trim());
        this.setBolonhaDegreeType(bolonhaDegreeType);
        this.setEctsCredits(ectsCredits);
        this.setPrevailingScientificArea(prevailingScientificArea.trim());
    }

    public void edit(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale) {
        commonFieldsChange(name, nameEn, gradeScale);
        oldStructureFieldsChange(code, degreeType);
        
        if(!hasAnyDegreeInfos())  
            new DegreeInfo(this);
    }

    public void edit(String name, String nameEn, String acronym, BolonhaDegreeType bolonhaDegreeType,
            Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea) {
        checkIfCanEdit(bolonhaDegreeType);
        commonFieldsChange(name, nameEn, gradeScale);
        newStructureFieldsChange(acronym, bolonhaDegreeType, ectsCredits, prevailingScientificArea);
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
            
            for (;!getParticipatingAnyCurricularCourseCurricularRules().isEmpty()
                 ;getParticipatingAnyCurricularCourseCurricularRules().get(0).delete());
            
            deleteDomainObject();
        } else {
            throw new DomainException("error.degree.has.degree.curricular.plans");
        }
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

    public void createDegreeCurricularPlan(String name, GradeScale gradeScale, Person creator, Role bolonhaRole) {
        for (DegreeCurricularPlan dcp : this.getDegreeCurricularPlans()) {
            if (dcp.getName().equalsIgnoreCase(name)) {
                throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
        
        if (!creator.hasPersonRoles(bolonhaRole)) {
            creator.addPersonRoles(bolonhaRole);
        }

        CurricularPeriod curricularPeriod = new CurricularPeriod(this.getBolonhaDegreeType().getCurricularPeriodType());
        
        new DegreeCurricularPlan(this, name, gradeScale, creator, curricularPeriod);
    }
    
    public boolean isBolonhaDegree() {
        return this.getBolonhaDegreeType() != null;
    }

    public String getPresentationName() {
    	final ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.EnumerationResources");
    	final ResourceBundle appResourceBundle = ResourceBundle.getBundle("resources.ApplicationResources");
    	return enumResourceBundle.getString(getTipoCurso().toString()) + " " + appResourceBundle.getString("label.in") + " " + getNome();
    }
    
    public List<DegreeCurricularPlan> findDegreeCurricularPlansByState(DegreeCurricularPlanState state){
    	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
    	if(!isBolonhaDegree()) {
    		for (DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlans()) {
    			if(degreeCurricularPlan.getState().equals(state)) {
    				result.add(degreeCurricularPlan);
    			}
    		}
    	}
    	return result;
    }
    
    // -------------------------------------------------------------
    // read static methods 
    // -------------------------------------------------------------

    public static Degree readBySigla(final String sigla) {
    	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
			if(degree.getSigla() != null && degree.getSigla().equalsIgnoreCase(sigla)) {
				return degree;
			}
		}
    	return null;
    }
    
    public static List<Degree> readAllFromOldDegreeStructure(){
    	List<Degree> result = new ArrayList<Degree>();
    	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
    		if(!degree.isBolonhaDegree()) {
    			result.add(degree);
    		}
    	}
    	return result;
    }
    
    public static List<Degree> readAllFromNewDegreeStructure(){
    	List<Degree> result = new ArrayList<Degree>();
    	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
    		if(degree.isBolonhaDegree()) {
    			result.add(degree);
    		}
    	}
    	return result;
    }
    
    public static List<Degree> readAllByDegreeType(final DegreeType degreeType){
    	List<Degree> result = new ArrayList<Degree>();
    	for (final Degree degree : RootDomainObject.getInstance().getDegrees()) {
    		if(degree.getTipoCurso() != null && degree.getTipoCurso() == degreeType) {
    			result.add(degree);
    		}
    	}
    	return result;
    }

    public static Degree getById(Integer id) {
        return RootDomainObject.getInstance().readDegreeByOID(id);
    }
}
