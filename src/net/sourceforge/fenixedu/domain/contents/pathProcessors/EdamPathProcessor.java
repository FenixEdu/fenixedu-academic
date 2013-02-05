package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.EdamSite;

public class EdamPathProcessor extends AbstractAcronymPathProcess<EdamSite> {

    private static final String ACRONYM = "EDAM";

    @Override
    protected String getAcronym() {
        return ACRONYM;
    }

}
