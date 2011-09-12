package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.util.Verhoeff;

import org.apache.commons.lang.StringUtils;

public class LibraryCardSystem extends LibraryCardSystem_Base {

    private static final String MILLENIUM_INSTITUTION_PREFIX = "0710";

    private static final int COUNTER_SIZE = 5;

    private static final String CODE_FILLER = "0";

    public  LibraryCardSystem() {
	super();
    }

    @Override
    public Group getHigherClearenceGroup() {
	Group group = super.getHigherClearenceGroup();
	if (group == null){
	    group = new FixedSetGroup();
	    setHigherClearenceGroup(group);
	}
	return group;
    }

    public String generateNewMilleniumCode() {
	// TODO: temporary:
	if (getMilleniumCodeCounter() == null) {
	    setMilleniumCodeCounter(44999);
	}
	setMilleniumCodeCounter(getMilleniumCodeCounter() + 1);
	String baseCode = MILLENIUM_INSTITUTION_PREFIX
		+ StringUtils.leftPad(Integer.toString(getMilleniumCodeCounter()), COUNTER_SIZE, CODE_FILLER);
	return baseCode + Verhoeff.generateVerhoeff(baseCode);
    }
}
