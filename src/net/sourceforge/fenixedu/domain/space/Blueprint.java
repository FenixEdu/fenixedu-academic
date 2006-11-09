package net.sourceforge.fenixedu.domain.space;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.joda.time.YearMonthDay;

public class Blueprint extends Blueprint_Base implements Comparable<Blueprint> {

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageBlueprints")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created space blueprint", parameters = {
	    "space", "person" })
    public Blueprint(Space space, Person person) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	checkNewBluePrintDates(space);
	closeCurrentSpaceBlueprint(space);
	setSpace(space);
	setCreationPerson(person);
	super.setValidFrom(new YearMonthDay());
    }

    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageBlueprints")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted space blueprint", parameters = {})
    public void delete() {
	Space space = getSpace();
	refreshBlueprintsDates(space);
	super.setSpace(null);
	super.setCreationPerson(null);
	removeBlueprintFile();
	removeRootDomainObject();
	openCurrentSpaceBlueprint(space);
	deleteDomainObject();
    }

    private void refreshBlueprintsDates(Space space) {
	SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(space.getBlueprints());
	if (!blueprints.isEmpty() && blueprints.last() != this) {
	    for (Iterator<Blueprint> iter = blueprints.iterator(); iter.hasNext();) {
		Blueprint blueprint = iter.next();
		if (blueprint == this) {
		    Blueprint nextBlueprint = iter.next();
		    nextBlueprint.updateValidFromDate(blueprint.getValidFrom());
		    break;
		}
	    }
	}
    }

    private void closeCurrentSpaceBlueprint(Space space) {
	SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(space.getBlueprints());
	if (!blueprints.isEmpty()) {
	    blueprints.last().closeBlueprint();
	}
    }

    private void openCurrentSpaceBlueprint(Space space) {
	SortedSet<Blueprint> blueprints = new TreeSet<Blueprint>(space.getBlueprints());
	if (!blueprints.isEmpty()) {
	    blueprints.last().openBlueprint();
	}
    }

    private void openBlueprint() {
	super.setValidUntil(null);
    }

    private void closeBlueprint() {
	super.setValidUntil(new YearMonthDay());
    }

    private void updateValidFromDate(YearMonthDay yearMonthDay) {
	super.setValidFrom(yearMonthDay);
    }

    @Override
    public void setValidFrom(YearMonthDay validFrom) {
	throw new DomainException("error.blueprint.invalid.validFrom.date");
    }

    @Override
    public void setValidUntil(YearMonthDay validUntil) {
	throw new DomainException("error.blueprint.invalid.validUntil.date");
    }

    @Override
    public void setCreationPerson(Person creationPerson) {
	if (creationPerson == null) {
	    throw new DomainException("error.blueprint.no.person");
	}
	super.setCreationPerson(creationPerson);
    }

    @Override
    public void setSpace(Space space) {
	if (space == null) {
	    throw new DomainException("error.blueprint.no.space");
	}
	super.setSpace(space);
    }

    private void checkNewBluePrintDates(Space space) {
	Blueprint mostRecentBlueprint = space.getMostRecentBlueprint();
	if (mostRecentBlueprint != null
		&& mostRecentBlueprint.getValidFrom().isEqual(new YearMonthDay())) {
	    throw new DomainException("error.blueprint.validFrom.date.already.exists");
	}
    }

    public int compareTo(Blueprint blueprint) {
	if (getValidUntil() == null) {
	    return 1;
	} else if (blueprint.getValidUntil() == null) {
	    return -1;
	} else {
	    return getValidUntil().compareTo(blueprint.getValidUntil());
	}
    }
}
