package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdProgram extends PhdProgram_Base {

    static public Comparator<PhdProgram> COMPARATOR_BY_NAME = new Comparator<PhdProgram>() {
        @Override
        public int compare(final PhdProgram p1, final PhdProgram p2) {
            int res = p1.getName().compareTo(p2.getName());
            return res != 0 ? res : DomainObjectUtil.COMPARATOR_BY_ID.compare(p1, p2);
        }
    };

    private PhdProgram() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        setCreator(Authenticate.getUser().getUsername());
        new PhdProgramServiceAgreementTemplate(this);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym) {
        this();

        checkDegree(degree);
        checkAcronym(acronym);

        setDegree(degree);
        setName(name);
        setAcronym(acronym);
    }

    private PhdProgram(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parentProgramUnit) {
        this(degree, name, acronym);
        PhdProgramUnit.create(this, getName(), getWhenCreated().toYearMonthDay(), null, parentProgramUnit);
    }

    private void checkDegree(final Degree degree) {
        String[] args = {};
        if (degree == null) {
            throw new DomainException("error.PhdProgram.invalid.degree", args);
        }
        if (degree.getDegreeType() != DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
            throw new DomainException("error.PhdProgram.invalid.degree");
        }
    }

    private void checkAcronym(final String acronym) {
        String[] args = {};
        if (acronym == null || acronym.isEmpty()) {
            throw new DomainException("error.PhdProgram.invalid.acronym", args);
        }
        final PhdProgram program = readByAcronym(acronym);
        if (program != null && program != this) {
            throw new DomainException("error.PhdProgram.acronym.already.exists", acronym);
        }
    }

    @Override
    public DegreeType getDegreeType() {
        return null;
    }

    @Override
    public Collection<CycleType> getCycleTypes() {
        return Collections.singletonList(CycleType.THIRD_CYCLE);
    }

    private boolean hasAcronym(final String acronym) {
        return getAcronym() != null && getAcronym().equalsIgnoreCase(acronym);
    }

    public String getPresentationName() {
        return getPresentationName(Language.getLocale());
    }

    private String getPresentationName(final Locale locale) {
        return getPrefix(locale) + getNameFor(locale);
    }

    private String getNameFor(final Locale locale) {
        final Language language = Language.valueOf(locale.getLanguage());
        return getName().hasContent(language) ? getName().getContent(language) : getName().getPreferedContent();
    }

    private String getPrefix(final Locale locale) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);
        return bundle.getString("label.php.program") + " " + bundle.getString("label.in") + " ";
    }

    @Override
    @Atomic
    public void delete() {
        if (hasAnyIndividualProgramProcesses()) {
            throw new DomainException("error.PhdProgram.cannot.delete.has.individual.php.program.processes");
        }

        getPhdProgramUnit().delete();
        setDegree(null);
        setServiceAgreementTemplate(null);
        setRootDomainObject(null);
        super.delete();
    }

    public Set<Person> getCoordinatorsFor(ExecutionYear executionYear) {
        if (!hasDegree()) {
            return Collections.emptySet();
        }

        final ExecutionDegree executionDegree =
                getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);

        final Set<Person> result = new HashSet<Person>();
        if (executionDegree != null) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                result.add(coordinator.getPerson());
            }
        }

        return result;
    }

    public Set<Person> getResponsibleCoordinatorsFor(ExecutionYear executionYear) {
        if (!hasDegree()) {
            return new HashSet<Person>();
        }

        final ExecutionDegree executionDegree =
                getDegree().getLastActiveDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);

        final Set<Person> result = new HashSet<Person>();
        if (executionDegree != null) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                if (coordinator.isResponsible()) {
                    result.add(coordinator.getPerson());
                }
            }
        }

        return result;
    }

    public boolean isCoordinatorFor(Person person, ExecutionYear executionYear) {
        return getCoordinatorsFor(executionYear).contains(person);
    }

    @Atomic
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym) {
        return new PhdProgram(degree, name, acronym);
    }

    @Atomic
    static public PhdProgram create(final Degree degree, final MultiLanguageString name, final String acronym, final Unit parent) {
        return new PhdProgram(degree, name, acronym, parent);
    }

    static public PhdProgram readByAcronym(final String acronym) {
        for (final PhdProgram program : Bennu.getInstance().getPhdProgramsSet()) {
            if (program.hasAcronym(acronym)) {
                return program;
            }
        }
        return null;
    }

    public PhdProgramContextPeriod getMostRecentPeriod() {
        List<PhdProgramContextPeriod> periods = new ArrayList<PhdProgramContextPeriod>();
        periods.addAll(getPhdProgramContextPeriods());

        Collections.sort(periods, Collections.reverseOrder(PhdProgramContextPeriod.COMPARATOR_BY_BEGIN_DATE));

        if (periods.isEmpty()) {
            return null;
        }

        return periods.iterator().next();
    }

    public boolean isActiveNow() {
        return isActive(new DateTime());
    }

    public boolean isActive(DateTime date) {
        for (PhdProgramContextPeriod period : getPhdProgramContextPeriods()) {
            if (period.contains(date)) {
                return true;
            }
        }

        return false;
    }

    public boolean isActive(final ExecutionYear executionYear) {
        PhdProgramContextPeriod mostRecentPeriod = getMostRecentPeriod();

        if (mostRecentPeriod.getEndDate() == null) {
            DateTime beginDate = mostRecentPeriod.getBeginDate();
            return beginDate.isBefore(executionYear.getBeginDateYearMonthDay().toDateMidnight())
                    || executionYear.containsDate(beginDate);
        }

        return mostRecentPeriod.getInterval().overlaps(executionYear.getAcademicInterval());
    }

    public PhdProgramInformation getMostRecentPhdProgramInformation() {
        return getPhdProgramInformationByDate(new LocalDate());
    }

    public PhdProgramInformation getPhdProgramInformationByDate(LocalDate localDate) {
        PhdProgramInformation mostRecent = null;

        for (PhdProgramInformation phdProgramInformation : getPhdProgramInformations()) {
            if (phdProgramInformation.getBeginDate().isAfter(localDate)) {
                continue;
            }

            if (mostRecent == null) {
                mostRecent = phdProgramInformation;
                continue;
            }

            if (phdProgramInformation.getBeginDate().isAfter(mostRecent.getBeginDate())) {
                mostRecent = phdProgramInformation;
            }
        }

        return mostRecent;
    }

    @Atomic
    public PhdProgramContextPeriod create(PhdProgramContextPeriodBean bean) {
        return PhdProgramContextPeriod.create(bean);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea> getPhdProgramFocusAreas() {
        return getPhdProgramFocusAreasSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramFocusAreas() {
        return !getPhdProgramFocusAreasSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess> getIndividualProgramProcesses() {
        return getIndividualProgramProcessesSet();
    }

    @Deprecated
    public boolean hasAnyIndividualProgramProcesses() {
        return !getIndividualProgramProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod> getInstitutionPhdCandidacyPeriod() {
        return getInstitutionPhdCandidacyPeriodSet();
    }

    @Deprecated
    public boolean hasAnyInstitutionPhdCandidacyPeriod() {
        return !getInstitutionPhdCandidacyPeriodSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail> getPhdProgramEmails() {
        return getPhdProgramEmailsSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramEmails() {
        return !getPhdProgramEmailsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod> getPhdProgramContextPeriods() {
        return getPhdProgramContextPeriodsSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramContextPeriods() {
        return !getPhdProgramContextPeriodsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramInformation> getPhdProgramInformations() {
        return getPhdProgramInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramInformations() {
        return !getPhdProgramInformationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

    @Deprecated
    public boolean hasPhdProgramUnit() {
        return getPhdProgramUnit() != null;
    }

}
