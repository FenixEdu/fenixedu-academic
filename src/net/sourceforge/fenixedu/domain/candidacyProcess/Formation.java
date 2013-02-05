package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Formatter;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Formation extends Formation_Base {

    public Formation() {
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

    public void exportValues(StringBuilder result) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
        Formatter formatter = new Formatter(result);
        formatter.format("\n%s:\n", bundle.getString("title.other.academic.titles"));
        formatter.format("%s: %s\n", bundle.getString("label.other.academic.titles.program.name"), getDesignation());
        formatter.format("%s: %s\n", bundle.getString("label.other.academic.titles.institution"), getInstitution().getName());
        formatter.format("%s: %s\n", bundle.getString("label.other.academic.titles.conclusion.date"),
                StringUtils.isEmpty(getYear()) ? StringUtils.EMPTY : getYear());
        formatter.format("%s: %s\n", bundle.getString("label.other.academic.titles.conclusion.grade"),
                StringUtils.isEmpty(getConclusionGrade()) ? StringUtils.EMPTY : getConclusionGrade());
    }

}
