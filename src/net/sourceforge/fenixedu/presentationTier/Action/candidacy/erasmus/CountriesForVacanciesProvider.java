package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusStudentDataBean;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;


import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountriesForVacanciesProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	ErasmusStudentDataBean bean = (ErasmusStudentDataBean) source;
	ErasmusCandidacyProcess process = (ErasmusCandidacyProcess) bean.getParentProcess();

	List<Country> countries = ((ErasmusCandidacyPeriod) process.getCandidacyPeriod()).getAssociatedVacancyCountries();
	Collections.sort(countries, new BeanComparator("localizedName"));

	return countries;
    }

}
