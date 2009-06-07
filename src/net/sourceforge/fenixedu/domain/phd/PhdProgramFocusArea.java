package net.sourceforge.fenixedu.domain.phd;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdProgramFocusArea extends PhdProgramFocusArea_Base {

    private PhdProgramFocusArea() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    public PhdProgramFocusArea(final MultiLanguageString name) {
	this();
	check(name, "error.PhdProgramFocusArea.invalid.name");
	checkForEqualFocusArea(name);
	setName(name);
    }

    private void checkForEqualFocusArea(final MultiLanguageString name) {
	for (final PhdProgramFocusArea focusArea : RootDomainObject.getInstance().getPhdProgramFocusAreasSet()) {
	    if (focusArea != this && focusArea.getName().equalInAnyLanguage(name)) {
		throw new DomainException("error.PhdProgramFocusArea.found.area.with.same.name");
	    }
	}
    }

    public void delete() {
	getPhdPrograms().clear();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
