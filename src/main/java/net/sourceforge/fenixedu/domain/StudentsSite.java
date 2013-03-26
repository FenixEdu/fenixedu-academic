package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class StudentsSite extends StudentsSite_Base {

    private StudentsSite(Unit unit) {
        super();
        setUnit(unit);
    }

}
