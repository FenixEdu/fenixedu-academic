package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.ResidenceInformationForm;

import org.apache.commons.beanutils.BeanComparator;

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
	    List<DistrictSubdivision> result = new ArrayList<DistrictSubdivision>(residenceInformationForm
		    .getDistrictOfResidence().getDistrictSubdivisions());
	    Collections.sort(result, new BeanComparator("name"));
	    return result;
	}

	return Collections.emptyList();
    }

}
