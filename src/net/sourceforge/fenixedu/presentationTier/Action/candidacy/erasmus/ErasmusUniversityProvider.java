package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentDataBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ErasmusUniversityProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        MobilityStudentDataBean bean = (MobilityStudentDataBean) source;
        Country selectedCountry = bean.getSelectedCountry();
        MobilityApplicationPeriod period = (MobilityApplicationPeriod) bean.getParentProcess().getCandidacyPeriod();

        List<UniversityUnit> universityUnitList = period.getUniversityUnitsAssociatedToCountry(selectedCountry);
        Collections.sort(universityUnitList, new BeanComparator("nameI18n"));

        return universityUnitList;
    }

}
