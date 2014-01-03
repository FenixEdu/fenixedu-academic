package net.sourceforge.fenixedu.domain.space;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;
import net.sourceforge.fenixedu.predicates.SpacePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class Blueprint extends Blueprint_Base implements Comparable<Blueprint> {

    @FenixDomainObjectActionLogAnnotation(actionName = "Created space blueprint", parameters = { "space", "person" })
    public Blueprint(Space space, Person person) {
        super();
        setRootDomainObject(Bennu.getInstance());
        checkNewBluePrintDates(space);
        closeCurrentSpaceBlueprint(space);
        setSpace(space);
        setCreationPerson(person);
        super.setValidFrom(new YearMonthDay());
    }

    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted space blueprint", parameters = {})
    public void delete() {
        check(this, SpacePredicates.checkIfLoggedPersonHasPermissionsToManageBlueprints);

        Space space = getSpace();
        refreshBlueprintsDates(space);
        super.setSpace(null);
        super.setCreationPerson(null);
        setBlueprintFile(null);
        setRootDomainObject(null);
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
        if (mostRecentBlueprint != null && mostRecentBlueprint.getValidFrom().isEqual(new YearMonthDay())) {
            throw new DomainException("error.blueprint.validFrom.date.already.exists");
        }
    }

    @Override
    public int compareTo(Blueprint blueprint) {
        if (getValidUntil() == null) {
            return 1;
        } else if (blueprint.getValidUntil() == null) {
            return -1;
        } else {
            return getValidUntil().compareTo(blueprint.getValidUntil());
        }
    }

    public static class BlueprintTextRectangles extends HashMap<Space, List<BlueprintTextRectangle>> {
    }

    public static class BlueprintTextRectangle {

        private final BlueprintPoint p1;

        private final BlueprintPoint p2;

        private final BlueprintPoint p3;

        private final BlueprintPoint p4;

        public BlueprintTextRectangle(String text, double x, double y, int fontSize) {

            double numberOfCharacters = text.length();
            double characterWidth = (fontSize / 1.6);
            double textSize = numberOfCharacters * characterWidth;

            p1 = new BlueprintPoint((int) x, (int) Math.round(y - fontSize));
            p2 = new BlueprintPoint((int) x, (int) y);
            p3 = new BlueprintPoint((int) Math.round(x + textSize), (int) y);
            p4 = new BlueprintPoint((int) Math.round(x + textSize), (int) Math.round(y - fontSize));

        }

        public BlueprintPoint getP1() {
            return p1;
        }

        public BlueprintPoint getP2() {
            return p2;
        }

        public BlueprintPoint getP3() {
            return p3;
        }

        public BlueprintPoint getP4() {
            return p4;
        }
    }

    public static class BlueprintPoint {

        private final int x;

        private final int y;

        public BlueprintPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    @Deprecated
    public boolean hasValidFrom() {
        return getValidFrom() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBlueprintFile() {
        return getBlueprintFile() != null;
    }

    @Deprecated
    public boolean hasValidUntil() {
        return getValidUntil() != null;
    }

    @Deprecated
    public boolean hasCreationPerson() {
        return getCreationPerson() != null;
    }

    @Deprecated
    public boolean hasSpace() {
        return getSpace() != null;
    }

}
