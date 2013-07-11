package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.util.phd.InstitutionPhdCandidacyProcessProperties;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InstitutionPhdCandidacyPeriod extends InstitutionPhdCandidacyPeriod_Base {

    protected InstitutionPhdCandidacyPeriod() {
        super();
    }

    protected InstitutionPhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end,
            final PhdCandidacyPeriodType type) {
        this();
        init(executionYear, start, end, type);
    }

    @Override
    protected void init(ExecutionYear executionYear, DateTime start, DateTime end, PhdCandidacyPeriodType type) {
        checkOverlapingDates(start, end, type);

        if (!PhdCandidacyPeriodType.INSTITUTION.equals(type)) {
            throw new DomainException("error.InstitutionPhdCandidacyPeriod.type.must.be.institution");
        }

        super.init(executionYear, start, end, type);
    }

    @Override
    public boolean isInstitutionCandidacyPeriod() {
        return true;
    }

    @Atomic
    public void addPhdProgramToPeriod(final PhdProgram phdProgram) {
        if (phdProgram == null) {
            throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
        }

        super.addPhdPrograms(phdProgram);
    }

    @Atomic
    public void addPhdProgramListToPeriod(final List<PhdProgram> phdProgramList) {
        super.getPhdProgramsSet().addAll(phdProgramList);
    }

    @Atomic
    public void removePhdProgramInPeriod(final PhdProgram phdProgram) {
        if (phdProgram == null) {
            throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
        }

        super.removePhdPrograms(phdProgram);
    }

    @Override
    public void addPhdPrograms(PhdProgram phdPrograms) {
        throw new DomainException("call addPhdProgramToPeriod()");
    }

    @Override
    public void removePhdPrograms(PhdProgram phdPrograms) {
        throw new DomainException("call removePhdProgramInPeriod()");
    }

    @Atomic
    public static InstitutionPhdCandidacyPeriod create(final PhdCandidacyPeriodBean phdCandidacyPeriodBean) {
        final ExecutionYear executionYear = phdCandidacyPeriodBean.getExecutionYear();
        final DateTime start = phdCandidacyPeriodBean.getStart();
        final DateTime end = phdCandidacyPeriodBean.getEnd();
        final PhdCandidacyPeriodType type = phdCandidacyPeriodBean.getType();

        return new InstitutionPhdCandidacyPeriod(executionYear, start, end, type);
    }

    public static InstitutionPhdCandidacyPeriod readInstitutionPhdCandidacyPeriodForDate(final DateTime date) {
        for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {
            if (period.isInstitutionCandidacyPeriod() && period.contains(date)) {
                return (InstitutionPhdCandidacyPeriod) period;
            }
        }

        return null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive(final DateTime date) {
        return readInstitutionPhdCandidacyPeriodForDate(date) != null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive() {
        return isAnyInstitutionPhdCandidacyPeriodActive(new DateTime());
    }

    static public InstitutionPhdCandidacyPeriod getMostRecentCandidacyPeriod() {
        PhdCandidacyPeriod mostRecentCandidacyPeriod = null;

        for (CandidacyPeriod candidacyPeriod : RootDomainObject.getInstance().getCandidacyPeriods()) {
            if (!candidacyPeriod.isInstitutionCandidacyPeriod()) {
                continue;
            }

            if (candidacyPeriod.getStart().isAfterNow()) {
                continue;
            }

            if (mostRecentCandidacyPeriod == null) {
                mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
                continue;
            }

            if (candidacyPeriod.getStart().isAfter(mostRecentCandidacyPeriod.getStart())) {
                mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
            }
        }

        return (InstitutionPhdCandidacyPeriod) mostRecentCandidacyPeriod;
    }

    @Override
    public String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee) {
        Locale locale = Language.getLocale();
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);

        return String.format(bundle.getString("message.phd.institution.email.body.referee"), referee
                .getPhdProgramCandidacyProcess().getPhdProgram().getName().getContent(Language.en),
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("en", "EN")),
                referee.getValue(), referee.getPhdProgramCandidacyProcess().getPhdProgram().getName().getContent(Language.pt),
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("pt", "PT")),
                referee.getValue());
    }

    public String getRefereeSubmissionFormLinkPt(final PhdCandidacyReferee referee) {
        return String.format("%s?hash=%s&locale=pt_PT",
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("pt", "PT")),
                referee.getValue());
    }

    public String getRefereeSubmissionFormLinkEn(final PhdCandidacyReferee referee) {
        return String.format("%s?hash=%s&locale=en_EN",
                InstitutionPhdCandidacyProcessProperties.getPublicCandidacyRefereeFormLink(new Locale("en", "EN")),
                referee.getValue());
    }

    @Override
    public MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final ResourceBundle englishBundle = getResourceBundle(Locale.ENGLISH);
        final ResourceBundle portugueseBundle = getResourceBundle();
        return new MultiLanguageString().with(Language.pt,
                portugueseBundle.getString("message.phd.institution.email.subject.missing.candidacy.validation")).with(
                Language.en, englishBundle.getString("message.phd.institution.email.subject.missing.candidacy.validation"));
    }

    @Override
    public MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final ResourceBundle englishBundle = getResourceBundle(Locale.ENGLISH);
        final ResourceBundle portugueseBundle = getResourceBundle();
        final String englishBody =
                String.format(englishBundle.getString("message.phd.institution.email.body.missing.candidacy.validation"),
                        PhdProperties.getPublicCandidacyAccessLink(), process.getCandidacyProcess().getCandidacyHashCode()
                                .getValue());
        final String portugueseBody =
                String.format(portugueseBundle.getString("message.phd.institution.email.body.missing.candidacy.validation"),
                        PhdProperties.getPublicCandidacyAccessLink(), process.getCandidacyProcess().getCandidacyHashCode()
                                .getValue());

        return new MultiLanguageString().with(Language.en, englishBody).with(Language.pt, portugueseBody);
    }

    final protected ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("resources.PhdResources");
    }

    final protected ResourceBundle getResourceBundle(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale);
    }

    public static InstitutionPhdCandidacyPeriod readNextCandidacyPeriod() {
        List<PhdCandidacyPeriod> readOrderedPhdCandidacyPeriods = readOrderedPhdCandidacyPeriods();

        for (PhdCandidacyPeriod phdCandidacyPeriod : readOrderedPhdCandidacyPeriods) {
            if (!phdCandidacyPeriod.isInstitutionCandidacyPeriod()) {
                continue;
            }

            if (phdCandidacyPeriod.getStart().isAfterNow()) {
                return (InstitutionPhdCandidacyPeriod) phdCandidacyPeriod;
            }
        }

        return null;
    }

}
