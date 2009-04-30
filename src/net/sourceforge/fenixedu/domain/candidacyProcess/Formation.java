package net.sourceforge.fenixedu.domain.candidacyProcess;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class Formation extends Formation_Base {
    
    public  Formation() {
        super();
    }
    
    public Formation(IndividualCandidacy individualCandidacy, FormationBean bean) {
	this();
	
	edit(bean);
	this.setIndividualCandidacy(individualCandidacy);
    }
    
    public void edit(FormationBean bean) {
	this.setBeginYear(bean.getFormationBeginYear());
	this.setBranch(null);
	this.setConcluded(bean.isConcluded());
	this.setCountry(null);
	this.setDateYearMonthDay(null);
	this.setDegree(null);
	this.setDegreeRecognition(null);
	this.setDesignation(bean.getDesignation());
	this.setEctsCredits(null);
	this.setEducationArea(null);
	this.setEquivalenceDateYearMonthDay(null);
	this.setEquivalenceSchool(null);
	this.setFormationHours(null);
	this.setFormationType(null);
	this.setInstitution(getOrCreateInstitution(bean));
	this.setMark(null);
	this.setPerson(null);
	this.setSchool(null);
	this.setSpecializationArea(null);
	this.setTitle(null);
	this.setType(null);
	this.setYear(bean.getFormationEndYear());
	this.setConclusionGrade(bean.getConclusionGrade());
	this.setConclusionExecutionYear(bean.getConclusionExecutionYear());
    }
    
    private Unit getOrCreateInstitution(final FormationBean bean) {
	if (StringUtils.isEmpty(bean.getInstitutionName()) && bean.getInstitutionUnit() != null) {
	    return bean.getInstitutionUnit();
	}

	if (bean.getInstitutionName() == null || bean.getInstitutionName().isEmpty()) {
	    throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
	}

	final Unit unit = Unit.findFirstExternalUnitByName(bean.getInstitutionName());
	return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
    }
    
}
