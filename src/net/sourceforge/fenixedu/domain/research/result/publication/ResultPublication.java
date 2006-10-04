package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;

public abstract class ResultPublication extends ResultPublication_Base {

    public enum ScopeType {
	LOCAL, NATIONAL, INTERNATIONAL;
    }

    /**
         * Comparator than can be used to order publications by Year.
         */
    static class OrderComparator implements Comparator<ResultPublication> {
	public int compare(ResultPublication rp1, ResultPublication rp2) {
	    Integer pub1 = rp1.getYear();
	    Integer pub2 = rp2.getYear();
	    if (pub1 == null) {
		return 1;
	    } else if (pub2 == null) {
		return -1;
	    }
	    return (-1) * pub1.compareTo(pub2);
	}
    }

    public static <T extends ResultPublication> List<T> sort(Collection<T> resultPublications) {
	List<T> sorted = new ArrayList<T>(resultPublications);
	Collections.sort(sorted, new ResultPublication.OrderComparator());

	return sorted;
    }

    public ResultPublication() {
	super();
    }

    private void removeAssociations() {
	super.setPublisher(null);
	super.setOrganization(null);
    }

    @Override
    @Checked("ResultPredicates.writePredicate")
    public void delete() {
	removeAssociations();
	super.delete();
    }

    @Override
    public void removePublisher() {
	throw new DomainException("error.researcher.ResultPublication.call", "removePublisher");
    }

    @Override
    public void removeOrganization() {
	throw new DomainException("error.researcher.ResultPublication.call", "removeOrganization");
    }

    public List<Person> getAuthors() {
	ArrayList<Person> authors = new ArrayList<Person>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Author))
		authors.add(participation.getPerson());
	}
	return authors;
    }

    public List<Person> getEditors() {
	ArrayList<Person> editors = new ArrayList<Person>();
	for (ResultParticipation participation : this.getResultParticipations()) {
	    if (participation.getRole().equals(ResultParticipationRole.Editor))
		editors.add(participation.getPerson());
	}
	return editors;
    }

    public abstract BibtexEntry exportToBibtexEntry();

    protected String generateBibtexKey() {
	String key = "";
	ResultParticipation participation = getOrderedResultParticipations().get(0);
	key = participation.getPerson().getName();
	key = key.replace(" ", "");
	if ((getYear() != null) && (getYear() > 0))
	    key = key + getYear();

	return key;
    }

    protected BibtexPersonList getBibtexAuthorsList(BibtexFile bibtexFile, List<Person> authors) {
	if ((authors != null) && (authors.size() > 0)) {
	    BibtexPersonList authorsList = bibtexFile.makePersonList();
	    BibtexPerson bp;
	    for (Person person : authors) {
		bp = bibtexFile.makePerson(person.getName(), null, null, null, false);
		authorsList.add(bp);
	    }
	    return authorsList;
	}
	return null;
    }

    protected BibtexPersonList getBibtexEditorsList(BibtexFile bibtexFile, List<Person> editors) {
	if ((editors != null) && (editors.size() > 0)) {
	    BibtexPersonList editorsList = bibtexFile.makePersonList();
	    BibtexPerson bp;
	    for (Person person : editors) {
		bp = bibtexFile.makePerson(person.getName(), null, null, null, false);
		editorsList.add(bp);
	    }
	    return editorsList;
	}
	return null;
    }

    /**
         * this methods are used instead of the provided by the javabib library
         * because in the javabib a BibtexPerson is printed using first lastName
         */
    protected String bibtexPersonToString(BibtexPerson bp) {
	String all = "";
	if (bp.isOthers()) {
	    all = "others";
	} else {
	    if (bp.getFirst() != null)
		all = all + bp.getFirst();
	    if (bp.getPreLast() != null)
		all = all + ' ' + bp.getPreLast();
	    if (bp.getLast() != null)
		all = all + ' ' + bp.getLast();
	    if (bp.getLineage() != null)
		all = all + ' ' + bp.getLineage();
	}
	return all;
    }

    protected String bibtexPersonListToString(BibtexPersonList bpl) {
	String personList = "";

	boolean isFirst = true;
	for (Iterator it = bpl.getList().iterator(); it.hasNext();) {
	    if (isFirst) {
		isFirst = false;
	    } else
		personList = personList + " and ";
	    personList = personList + bibtexPersonToString((BibtexPerson) it.next());
	}
	return personList;
    }
}
