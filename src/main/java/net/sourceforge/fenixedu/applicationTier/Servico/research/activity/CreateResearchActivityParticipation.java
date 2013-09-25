package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.predicates.ResultPredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateResearchActivityParticipation {

    @Atomic
    public static void run(ResearchEvent event, ResearchActivityParticipationRole role, Person person,
            MultiLanguageString roleMessage) throws FenixServiceException {
        check(ResultPredicates.author);

        new EventParticipation(person, role, event, roleMessage);
    }

    @Atomic
    public static void run(ScientificJournal journal, ResearchActivityParticipationRole role, Person person,
            MultiLanguageString roleMessage, YearMonthDay begin, YearMonthDay end) throws FenixServiceException {
        check(ResultPredicates.author);

        new ScientificJournalParticipation(person, role, journal, roleMessage, begin, end);
    }

    @Atomic
    public static void run(ScientificJournal journal, ParticipantBean bean) throws FenixServiceException {
        check(ResultPredicates.author);
        Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());

        new ScientificJournalParticipation(unit, bean.getRole(), journal, bean.getRoleMessage(), bean.getBeginDate(),
                bean.getEndDate());
    }

    @Atomic
    public static void run(ResearchEvent event, ParticipantBean bean) throws FenixServiceException {
        check(ResultPredicates.author);

        Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
        new EventParticipation(unit, bean.getRole(), event, bean.getRoleMessage());
    }

    @Atomic
    public static void run(JournalIssue issue, ResearchActivityParticipationRole role, Person person,
            MultiLanguageString roleMessage) {
        check(ResultPredicates.author);
        new JournalIssueParticipation(issue, role, person, roleMessage);
    }

    @Atomic
    public static void run(EventEdition edition, ResearchActivityParticipationRole role, Person person,
            MultiLanguageString roleMessage) {
        check(ResultPredicates.author);
        new EventEditionParticipation(person, role, edition, roleMessage);
    }

    @Atomic
    public static void run(ResearchCooperationCreationBean cooperationBean, Person person) throws FenixServiceException {
        check(ResultPredicates.author);

        Unit unit = getUnit(cooperationBean.getUnit(), cooperationBean.getUnitName(), cooperationBean.isExternalParticipation());

        Cooperation cooperation =
                new Cooperation(cooperationBean.getRole(), person, cooperationBean.getCooperationName(),
                        cooperationBean.getType(), unit, cooperationBean.getStartDate(), cooperationBean.getEndDate());

        new CooperationParticipation(person, cooperationBean.getRole(), cooperation, cooperationBean.getRoleMessage());
    }

    private static Unit getUnit(Unit unit, String unitName, boolean isExternalUnit) throws FenixServiceException {
        if (unit == null) {
            if (isExternalUnit) {
                return Unit.createNewNoOfficialExternalInstitution(unitName);
            } else {
                throw new FenixServiceException("error.researcher.ResearchActivityParticipation.unitMustBeExternal");
            }
        } else {
            return unit;
        }

    }
}