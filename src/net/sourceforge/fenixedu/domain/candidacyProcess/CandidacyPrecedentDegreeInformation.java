package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

abstract public class CandidacyPrecedentDegreeInformation extends CandidacyPrecedentDegreeInformation_Base {

    protected CandidacyPrecedentDegreeInformation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean hasInstitution() {
	return getInstitution() != null;
    }

    public boolean isInternal() {
	return false;
    }

    public boolean isExternal() {
	return false;
    }

    abstract public String getDegreeDesignation();

    abstract protected Integer getConclusionYear();

    abstract public LocalDate getConclusionDate();

    public boolean hasConclusionDate() {
	return getConclusionDate() != null;
    }

    abstract public Unit getInstitution();

    abstract public String getConclusionGrade();

    abstract public void edit(final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation);

    abstract public void editCurricularCoursesInformation(final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation);

    public String getDegreeAndInstitutionName() {
	return getDegreeDesignation() + " / " + getInstitution().getName();
    }

    abstract public Integer getNumberOfEnroledCurricularCourses();

    abstract public Integer getNumberOfApprovedCurricularCourses();

    abstract public BigDecimal getGradeSum();

    abstract public BigDecimal getApprovedEcts();

    abstract public BigDecimal getEnroledEcts();

    public void fill(final CandidacyInformationBean bean) {
	bean.setSchoolLevel(getSchoolLevel());
	bean.setOtherSchoolLevel(getOtherSchoolLevel());
	bean.setConclusionGrade(getConclusionGrade());
	bean.setConclusionYear(getConclusionYear());
	bean.setInstitution(getInstitution());
	bean.setDegreeDesignation(getDegreeDesignation());
    }

    public void edit(final CandidacyInformationBean bean) {
	setSchoolLevel(bean.getSchoolLevel());
	setOtherSchoolLevel(bean.getOtherSchoolLevel());
    }

    public void editMissingInformation(final CandidacyInformationBean bean) {
	setSchoolLevel(hasSchoolLevel() ? getSchoolLevel() : bean.getSchoolLevel());
	setOtherSchoolLevel(hasOtherSchoolLevel() ? getOtherSchoolLevel() : bean.getOtherSchoolLevel());
    }

    protected boolean hasSchoolLevel() {
	return getSchoolLevel() != null;
    }

    protected boolean hasOtherSchoolLevel() {
	return getOtherSchoolLevel() != null && !getOtherSchoolLevel().isEmpty();
    }

}
