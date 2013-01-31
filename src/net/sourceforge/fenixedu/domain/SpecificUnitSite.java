package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class SpecificUnitSite extends SpecificUnitSite_Base {

	protected SpecificUnitSite() {
		super();
	}

	public SpecificUnitSite(Unit unit) {
		super();
		setUnit(unit);
	}

	@Override
	public IGroup getOwner() {
		return new GroupUnion(new FixedSetGroup(getManagers()));
	}

	@Override
	public void appendReversePathPart(final StringBuilder stringBuilder) {
	}

	@Override
	public MultiLanguageString getName() {
		return new MultiLanguageString("");
	}
}
