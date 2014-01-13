package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceInformationForm;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictsForResidenceInformationFormProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final ResidenceInformationForm residenceInformationForm = (ResidenceInformationForm) source;
        final List<District> result = new ArrayList<District>();
        if (residenceInformationForm.getCountryOfResidence().isDefaultCountry()) {
            for (District district : Bennu.getInstance().getDistrictsSet()) {
                if (!district.getName().equals("Estrangeiro")) {
                    result.add(district);
                }
            }
        } else {
            for (District district : Bennu.getInstance().getDistrictsSet()) {
                if (district.getName().equals("Estrangeiro")) {
                    result.add(district);
                    return result;
                }
            }
        }

        //Collections.sort(result, new BeanComparator("name"));
        return result;
    }

}
