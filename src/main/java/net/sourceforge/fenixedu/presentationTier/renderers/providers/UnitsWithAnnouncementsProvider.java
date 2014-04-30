package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnitsWithAnnouncementsProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        Set<Unit> result = new HashSet<Unit>();

        for (final AnnouncementBoard board : Bennu.getInstance().getAnnouncementBoardSet()) {
            if (!(board instanceof UnitAnnouncementBoard)) {
                continue;
            }
            UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
            result.add(unitBoard.getUnit());
        }

        return result;
    }

}
