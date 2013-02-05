package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.berserk.logic.filterManager.IFilter;

abstract public class Filter implements IFilter {

    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

}
