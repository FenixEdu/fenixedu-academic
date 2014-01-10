package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.District;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictsProviderAsString implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<String> result = new ArrayList<String>();

        Collator ptsCollator = Collator.getInstance(new Locale("pt"));
        for (final District each : Bennu.getInstance().getDistrictsSet()) {
            result.add(each.getName());
        }

        Collections.sort(result, ptsCollator);
        return result;
    }

}
