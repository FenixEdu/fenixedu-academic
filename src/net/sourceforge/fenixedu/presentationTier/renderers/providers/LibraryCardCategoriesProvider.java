package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class LibraryCardCategoriesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        List<PartyClassification> partyClassifications = new ArrayList<PartyClassification>();
        for (PartyClassification classification : Arrays.asList(PartyClassification.values())) {
            if(!classification.equals(PartyClassification.UNIT)) {
                partyClassifications.add(classification);
            }
        }
        return partyClassifications;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }
}
