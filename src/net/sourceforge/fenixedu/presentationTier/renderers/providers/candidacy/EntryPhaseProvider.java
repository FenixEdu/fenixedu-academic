package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Arrays;

import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.domain.EntryPhase;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EntryPhaseProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        IngressionInformationBean ingressionInformationBean = (IngressionInformationBean) source;
        if (ingressionInformationBean.getIngression() != null && ingressionInformationBean.getIngression().hasEntryPhase()) {
            return Arrays.asList(EntryPhase.values());
        }
        return new ArrayList<EntryPhase>();
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
