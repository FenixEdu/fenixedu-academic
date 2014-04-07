package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.util.phd.EPFLPhdCandidacyProcessProperties;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EPFLPhdCandidacyPeriod extends EPFLPhdCandidacyPeriod_Base {

    protected EPFLPhdCandidacyPeriod() {
        super();
    }

    protected EPFLPhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end,
            PhdCandidacyPeriodType type) {
        this();

        init(executionYear, start, end, type);
    }

    @Override
    protected void init(final ExecutionYear executionYear, final DateTime start, final DateTime end, PhdCandidacyPeriodType type) {
        checkIfCanCreate(start, end);

        if (!PhdCandidacyPeriodType.EPFL.equals(type)) {
            throw new DomainException("error.EPFLPhdCandidacyPeriod.type.must.be.epfl");
        }

        super.init(executionYear, start, end, type);
    }

    private void checkIfCanCreate(final DateTime start, final DateTime end) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (!period.equals(this) && period.isEpflCandidacyPeriod() && period.intercept(start, end)) {
                throw new DomainException(
                        "error.EPFLInstitutionPhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
            }
        }
    }

    @Override
    public boolean isEpflCandidacyPeriod() {
        return true;
    }

    @Atomic
    public static EPFLPhdCandidacyPeriod create(final PhdCandidacyPeriodBean phdCandidacyPeriodBean) {
        final ExecutionYear executionYear = phdCandidacyPeriodBean.getExecutionYear();
        final DateTime start = phdCandidacyPeriodBean.getStart();
        final DateTime end = phdCandidacyPeriodBean.getEnd();
        final PhdCandidacyPeriodType type = phdCandidacyPeriodBean.getType();

        return new EPFLPhdCandidacyPeriod(executionYear, start, end, type);
    }

    public static boolean isAnyEPFLPhdCandidacyPeriodActive() {
        return isAnyEPFLPhdCandidacyPeriodActive(new DateTime());
    }

    public static boolean isAnyEPFLPhdCandidacyPeriodActive(final DateTime date) {
        return readEPFLPhdCandidacyPeriodForDateTime(date) != null;
    }

    public static EPFLPhdCandidacyPeriod readEPFLPhdCandidacyPeriodForDateTime(final DateTime date) {
        for (final CandidacyPeriod period : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (period.isEpflCandidacyPeriod() && period.contains(date)) {
                return (EPFLPhdCandidacyPeriod) period;
            }
        }

        return null;
    }

    static public EPFLPhdCandidacyPeriod getMostRecentCandidacyPeriod() {
        PhdCandidacyPeriod mostRecentCandidacyPeriod = null;

        for (CandidacyPeriod candidacyPeriod : Bennu.getInstance().getCandidacyPeriodsSet()) {
            if (!candidacyPeriod.isEpflCandidacyPeriod()) {
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

        return (EPFLPhdCandidacyPeriod) mostRecentCandidacyPeriod;
    }

    @Override
    public String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee) {
        Locale locale = Language.getLocale();
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", locale);

        return String.format(bundle.getString("message.phd.epfl.email.body.referee"), referee.getPhdProgramCandidacyProcess()
                .getIndividualProgramProcess().getPhdProgramFocusArea().getName().getContent(), EPFLPhdCandidacyProcessProperties
                .getConfiguration().getPublicCandidacyRefereeFormLink(), referee.getValue(), referee
                .getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().getEnd().toString("yyyy-MM-dd HH:mm"));
    }

    @Override
    public MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
        return new MultiLanguageString().with(Language.en,
                bundle.getString("message.phd.epfl.email.subject.missing.candidacy.validation"));
    }

    @Override
    public MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(PhdIndividualProgramProcess process) {
        final ResourceBundle bundle = getResourceBundle(Locale.ENGLISH);
        final String body =
                String.format(bundle.getString("message.phd.epfl.email.body.missing.candidacy.validation"),
                        PhdProperties.getPublicCandidacyAccessLink(), process.getCandidacyProcess().getCandidacyHashCode()
                                .getValue());
        return new MultiLanguageString().with(Language.en, body);
    }

    final protected ResourceBundle getResourceBundle(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale);
    }
}
