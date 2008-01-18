package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;


public class ResearchUnitPathProcessor extends AbstractUnitAcronymPathProcessor {

    @Override
    protected Class[] getAcceptableTypes() {
	return new Class[] { ResearchUnit.class };
    }

  
}
