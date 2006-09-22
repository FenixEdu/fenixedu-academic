package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class PersonsFoundForBibtexPerson implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        final BibtexParticipatorBean bibtexParticipatorBean = (BibtexParticipatorBean) source;
        return bibtexParticipatorBean.getPersonsFound();
    }

    public Converter getConverter() {
        return null;
    }

}
