package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class UnitsWithAnnouncementsProvider implements DataProvider {

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
        Set<Unit> result = new HashSet<Unit>();
        
        for (AnnouncementBoard board : RootDomainObject.getInstance().getAnnouncementBoards()) {
            if (! (board instanceof UnitAnnouncementBoard)) {
                continue;
            }
            
            UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
            result.add(unitBoard.getUnit());
        }
        
        return result;
    }

}

