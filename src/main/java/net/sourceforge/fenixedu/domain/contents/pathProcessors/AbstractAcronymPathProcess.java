package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;
import pt.ist.bennu.core.domain.Bennu;

public abstract class AbstractAcronymPathProcess<T extends Site> extends AbstractPathProcessor {

    protected T site = null;

    @Override
    public Content processPath(String path) {
        return getSite();
    }

    private Content getSite() {
        if (site == null) {
            for (UnitAcronym acronym : Bennu.getInstance().getUnitAcronymsSet()) {
                if (acronym.getAcronym().equalsIgnoreCase(getAcronym())) {
                    Unit unit = acronym.getUnits().iterator().next();
                    site = (T) unit.getSite();
                    return site;
                }
            }
        }
        return site;
    }

    protected abstract String getAcronym();

    @Override
    public String getTrailingPath(String path) {
        return path;
    }

    @Override
    public Content getInitialContent() {
        return getSite();
    }

    @Override
    public boolean keepPortalInContentsPath() {
        return false;
    }
}
