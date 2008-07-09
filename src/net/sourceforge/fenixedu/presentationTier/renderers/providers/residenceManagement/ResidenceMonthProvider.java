package net.sourceforge.fenixedu.presentationTier.renderers.providers.residenceManagement;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ResidenceMonthProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	ImportResidenceEventBean residenceEventBean = (ImportResidenceEventBean) source;
	ResidenceYear residenceYear = residenceEventBean.getResidenceYear();
	return residenceYear != null ? residenceYear.getMonthsSet() : Collections
		.emptyList();
    }

}
