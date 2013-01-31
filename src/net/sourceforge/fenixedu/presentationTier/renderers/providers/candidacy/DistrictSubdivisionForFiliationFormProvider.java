package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.candidacy.workflow.form.FiliationForm;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictSubdivisionForFiliationFormProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		final FiliationForm filiationForm = (FiliationForm) source;
		if (filiationForm.getDistrictOfBirth() != null) {
			final District district = District.readByName(filiationForm.getDistrictOfBirth());

			if (district != null) {
				return transformToStringCollection(district);
			}
		}

		return Collections.emptyList();
	}

	private Set<String> transformToStringCollection(final District district) {
		final SortedSet<String> result = new TreeSet<String>();

		for (final DistrictSubdivision each : district.getDistrictSubdivisions()) {
			result.add(each.getName());
		}

		return result;
	}

}
