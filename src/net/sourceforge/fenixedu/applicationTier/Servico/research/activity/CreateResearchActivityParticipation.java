package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityUnitParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateResearchActivityParticipation extends Service {
	
	public void run(Event event, ResearchActivityParticipationRole role, Person person)
			throws ExcepcaoPersistencia, FenixServiceException {

		new Participation(person, role, event);
	}

	public void run(ScientificJournal journal, ResearchActivityParticipationRole role, Person person)
			throws ExcepcaoPersistencia, FenixServiceException {

		new Participation(person, role, journal);
	}

	public void run(ScientificJournal journal, ResearchActivityParticipantBean bean)
			throws FenixServiceException {
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
		
		new Participation(unit, bean.getRole(), journal);
	}
	
	public void run(Event event, ResearchActivityParticipantBean bean) throws ExcepcaoPersistencia,
		FenixServiceException {
	
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
		new Participation(unit, bean.getRole(), event);
	}
	
	public void run(ScientificJournal journal, ResearchActivityUnitParticipantBean bean)
		throws FenixServiceException {
		
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
	
		new Participation(unit, bean.getRole(), journal);
	}
		
	public void run(Event event, ResearchActivityUnitParticipantBean bean) throws ExcepcaoPersistencia,
		FenixServiceException {
		
		Unit unit = getUnit(bean.getUnit(), bean.getUnitName(), bean.isExternalParticipation());
		new Participation(unit, bean.getRole(), event);
	}
	
	public void run(ResearchCooperationCreationBean cooperationBean, Person person) 
	throws ExcepcaoPersistencia, FenixServiceException {

		Unit unit = getUnit(cooperationBean.getUnit(), cooperationBean.getUnitName(), cooperationBean.isExternalParticipation());
		
		Cooperation cooperation = new Cooperation(cooperationBean.getRole(), person, new MultiLanguageString(cooperationBean.getCooperationName()), 
				cooperationBean.getType(), unit, cooperationBean.getStartDate(), cooperationBean.getEndDate());
		
		new Participation(person, cooperationBean.getRole(), cooperation);	
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
