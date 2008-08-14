package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnitsWithAnnouncementsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	Set<Unit> result = new HashSet<Unit>();

	for (final Content content : RootDomainObject.getInstance().getContentsSet()) {
	    if (!(content instanceof UnitAnnouncementBoard)) {
		continue;
	    }
	    UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) content;
	    result.add(unitBoard.getUnit());
	}

	return result;
    }

}
