package net.sourceforge.fenixedu.domain.research;

import java.util.ArrayList;
import java.util.List;

import dml.runtime.RelationAdapter;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class Prize extends Prize_Base {

	static {
		PrizeWinners.addListener(new RelationAdapter<Prize, Party>() {
			@Override
			public void afterRemove(Prize prize, Party party) {
				if (prize != null && party != null) {
					if (prize.getParties().isEmpty()) {
						prize.delete();
					}
				}
				super.afterRemove(prize, party);
			}
		});
	}

	private Prize() {
		super();
		this.setRootDomainObject(RootDomainObject.getInstance());
	}

	public Prize(String name, MultiLanguageString description, Integer year) {
		this();
		setName(name);
		setYear(year);
		setDescription(description);
	}

	public Prize(String name, MultiLanguageString description, Integer year, Person person) {
		this(name, description, year);
		addPerson(person);
	}

	public Prize(String name, MultiLanguageString description, Integer year, Unit unit) {
		this(name, description, year);
		addUnit(unit);
	}

	public Prize(String name, MultiLanguageString description, Integer year, ResearchResult result) {
		this(name, description, year);
		for (ResultParticipation participation : result.getResultParticipations()) {
			addPerson(participation.getPerson());
		}
		setResearchResult(result);
	}

	@Override
	public void addParties(Party party) {
		if (this.getParties().contains(party)) {
			throw new DomainException("error.party.already.present");
		}
		super.addParties(party);
	}

	public void addUnit(Unit unit) {
		addParties(unit);
	}

	public void addPerson(Person person) {
		addParties(person);
	}

	public List<Person> getPeople() {
		List<Person> people = new ArrayList<Person>();
		for (Party party : getParties()) {
			if (party.isPerson()) {
				people.add((Person) party);
			}
		}
		return people;
	}

	public List<Unit> getUnits() {
		List<Unit> units = new ArrayList<Unit>();
		for (Party party : getParties()) {
			if (party.isUnit()) {
				units.add((Unit) party);
			}
		}
		return units;
	}

	public boolean isDeletableByCurrentUser() {
		return isDeletableByUser(AccessControl.getPerson());
	}

	public boolean isDeletableByUser(Person person) {
		if (!hasResearchResult()) {
			return getPeople().contains(person);
		} else {
			return getResearchResult().isDeletableByUser(person);
		}
	}

	public boolean isEditableByUser(Person person) {
		if (!hasResearchResult()) {
			return getPeople().contains(person);
		} else {
			return getResearchResult().isEditableByUser(person);
		}
	}

	public boolean isEditableByCurrentUser() {
		return isEditableByUser(AccessControl.getPerson());
	}

	public void delete() {
		removeResearchResult();
		for (; !getParties().isEmpty(); getParties().get(0).removePrizes(this));
		removeRootDomainObject();
		super.deleteDomainObject();
	}
	
	public ResearchResultPublication getResearchResultPublication() {
		return isAssociatedToPublication() ? (ResearchResultPublication) getResearchResult() : null;
	}
	
	public ResearchResultPatent getResearchResultPatent() {
		return isAssociatedToPatent() ? (ResearchResultPatent) getResearchResult() : null;
	}
	
	public boolean isAssociatedToPatent() {
		return getResearchResult() instanceof ResearchResultPatent;
	}
	
	public boolean isAssociatedToPublication() {
		return getResearchResult() instanceof ResearchResultPublication;
	}
}
