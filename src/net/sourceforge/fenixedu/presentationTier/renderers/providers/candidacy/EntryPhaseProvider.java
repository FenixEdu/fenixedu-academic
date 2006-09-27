package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.EntryPhase;

public class EntryPhaseProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) source;
	if (ingressionInformationBean.getIngression() != null
		&& ingressionInformationBean.getIngression().hasEntryPhase()) {
	    return EntryPhase.getAll();
	}
	return new ArrayList<EntryPhase>();
    }

    public Converter getConverter() {
	return null;
    }

}
