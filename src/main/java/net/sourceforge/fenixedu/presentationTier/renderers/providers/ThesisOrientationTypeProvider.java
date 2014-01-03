package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ThesisOrientationTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ThesisParticipationType> thesisParticipationTypes = new ArrayList<ThesisParticipationType>();
        thesisParticipationTypes.add(ThesisParticipationType.ORIENTATOR);
        thesisParticipationTypes.add(ThesisParticipationType.COORIENTATOR);

        return thesisParticipationTypes;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
