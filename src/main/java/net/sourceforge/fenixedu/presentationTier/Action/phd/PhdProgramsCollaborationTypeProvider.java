package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdProgramsCollaborationTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {
        return PhdIndividualProgramCollaborationType.valuesAsList();
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}
