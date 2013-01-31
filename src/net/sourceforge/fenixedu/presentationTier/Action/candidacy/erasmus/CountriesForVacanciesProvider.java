package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentDataBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountriesForVacanciesProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		MobilityStudentDataBean bean = (MobilityStudentDataBean) source;
		MobilityApplicationProcess process = (MobilityApplicationProcess) bean.getParentProcess();

		List<Country> countries = process.getCandidacyPeriod().getAssociatedCountries();
		Collections.sort(countries, new BeanComparator("localizedName"));

		return countries;
	}

}
