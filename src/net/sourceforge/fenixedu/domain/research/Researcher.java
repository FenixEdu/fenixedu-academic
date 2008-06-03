package net.sourceforge.fenixedu.domain.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Researcher extends Researcher_Base {

    public static Comparator<Researcher> PUBLICATION_VOLUME_COMPARATOR = new ReverseComparator(new Comparator<Researcher>() {

	public int compare(Researcher r1, Researcher r2) {
	    Integer resultParticipationsCount = r1.getPerson().getResultParticipationsCount();
	    Integer resultParticipationsCount2 = r2.getPerson().getResultParticipationsCount();
	    return resultParticipationsCount.compareTo(resultParticipationsCount2);
	}

    });

    public Researcher(Person person) {
	super();
	setPerson(person);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean hasAtLeastOneKeyword(String... keywords) {
	for(String keyword : keywords) {
	    if (hasKeyword(keyword)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasKeyword(String keyword) {
	String trimmedKeyword = keyword.trim();
	for (String reseacherKeyword : getKeywords().split(",")) {
	    if (trimmedKeyword.equalsIgnoreCase(reseacherKeyword.trim())) {
		return true;
	    }
	}
	return false;
    }

    public List<ResearchInterest> getResearchInterests() {
	List<ResearchInterest> orderedInterests = new ArrayList<ResearchInterest>(getPerson().getResearchInterests());
	Collections.sort(orderedInterests, new Comparator<ResearchInterest>() {
	    public int compare(ResearchInterest researchInterest1, ResearchInterest researchInterest2) {
		return researchInterest1.getInterestOrder().compareTo(researchInterest2.getInterestOrder());
	    }
	});

	return orderedInterests;
    }
}
