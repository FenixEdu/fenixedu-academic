package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceInformationForm;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictSubdivisionForResidenceInformationFormProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final ResidenceInformationForm residenceInformationForm = (ResidenceInformationForm) source;
	if (residenceInformationForm.getDistrictOfResidence() != null) {
	    return residenceInformationForm.getDistrictOfResidence().getDistrictSubdivisions();
	}

	return Collections.emptyList();
    }

}
