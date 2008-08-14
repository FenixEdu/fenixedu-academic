package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;

public class ScientificAreaPathProcessor extends AbstractUnitAcronymPathProcessor {

    @Override
    protected Class[] getAcceptableTypes() {
	return new Class[] { DepartmentUnit.class, ScientificAreaUnit.class };
    }

}
