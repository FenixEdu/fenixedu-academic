package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.math.BigDecimal;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.LocalDate;

@Deprecated
abstract public class CandidacyPrecedentDegreeInformation extends CandidacyPrecedentDegreeInformation_Base {

    protected CandidacyPrecedentDegreeInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    public String getDegreeAndInstitutionName() {
        return getDegreeDesignation() + " / " + getInstitution().getName();
    }

    abstract public Integer getNumberOfEnroledCurricularCourses();

    abstract public Integer getNumberOfApprovedCurricularCourses();

    abstract public BigDecimal getGradeSum();

    abstract public BigDecimal getApprovedEcts();

    abstract public BigDecimal getEnroledEcts();

    protected boolean hasSchoolLevel() {
        return getSchoolLevel() != null;
    }

    protected boolean hasOtherSchoolLevel() {
        return getOtherSchoolLevel() != null && !getOtherSchoolLevel().isEmpty();
    }

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

}
