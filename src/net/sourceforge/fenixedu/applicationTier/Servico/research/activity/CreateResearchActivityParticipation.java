package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResearchActivityParticipation extends Service {
	
	public void run(Event event, ResearchActivityParticipationRole role, Person person)
			throws ExcepcaoPersistencia, FenixServiceException {

		new EventParticipation(person, role, event);
	}

	public void run(ScientificJournal journal, ResearchActivityParticipationRole role, Person person)
			throws ExcepcaoPersistencia, FenixServiceException {

		new ScientificJournalParticipation(person, role, journal);
	}

	public void run(ScientificJournal journal, ParticipantBean bean)
			throws FenixServiceException {
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
		
		new ScientificJournalParticipation(unit, bean.getRole(), journal);
	}
	
	public void run(Event event, ParticipantBean bean) throws ExcepcaoPersistencia,
		FenixServiceException {
	
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
		new EventParticipation(unit, bean.getRole(), event);
	}
	
	public void run(JournalIssue issue, ResearchActivityParticipationRole role, Person person) {
	    new JournalIssueParticipation(issue,role,person);
	}
	
	public void run(EventEdition edition, ResearchActivityParticipationRole role, Person person) {
	    new EventEditionParticipation(person,role,edition);
	}
		
	public void run(ResearchCooperationCreationBean cooperationBean, Person person) 
	throws ExcepcaoPersistencia, FenixServiceException {

		Unit unit = getUnit(cooperationBean.getUnit(), cooperationBean.getUnitName(), cooperationBean.isExternalParticipation());
		
		Cooperation cooperation = new Cooperation(cooperationBean.getRole(), person, cooperationBean.getCooperationName(), 
				cooperationBean.getType(), unit, cooperationBean.getStartDate(), cooperationBean.getEndDate());
		
		new CooperationParticipation(person, cooperationBean.getRole(), cooperation);	
	}
	
	private Unit getUnit(Unit unit, String unitName, boolean isExternalUnit) throws FenixServiceException {
		if (unit == null) {
			if (isExternalUnit) {
				return Unit.createNewExternalInstitution(unitName);
			} else {
				throw new FenixServiceException("error.researcher.ResearchActivityParticipation.unitMustBeExternal");
			}
		} else {
			return unit;
		}

	}
}
