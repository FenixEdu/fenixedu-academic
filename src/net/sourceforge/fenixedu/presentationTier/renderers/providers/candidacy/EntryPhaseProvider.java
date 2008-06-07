package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.util.EntryPhase;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

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
