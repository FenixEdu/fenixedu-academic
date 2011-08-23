package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeSite extends DegreeSite_Base {

    public DegreeSite(Degree degree) {
	super();

	setDegree(degree);
    }

    @Override
    public IGroup getOwner() {
	return new CurrentDegreeCoordinatorsGroup(getDegree());
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
	List<IGroup> groups = super.getContextualPermissionGroups();

	groups.add(new CurrentDegreeCoordinatorsGroup(getDegree()));
	groups.add(new RoleGroup(RoleType.TEACHER));

	return groups;
    }

    @Override
    protected void disconnect() {
	removeDegree();
	super.disconnect();
    }

    @Override
    public MultiLanguageString getName() {
	final Degree degree = getDegree();
	final String name = degree.getSigla();
	return MultiLanguageString.i18n().add("pt", name).finish();
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
	// unable to optimize because we cannot track changes to name correctly.
	// don't call super.setNormalizedName() !
    }

    public Unit getUnit() {
	Unit unit = super.getUnit();
	if (unit == null) {
	    if (hasDegree()) {
		unit = getDegree().getUnit();
		updateUnit(unit);
	    }
	}
	return unit;
    }

    @Service
    private void updateUnit(Unit unit) {
	setUnit(unit);
    }

}
