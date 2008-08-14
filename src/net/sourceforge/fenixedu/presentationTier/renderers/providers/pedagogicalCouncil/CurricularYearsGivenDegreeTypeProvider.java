package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularYearsGivenDegreeTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ElectionPeriodBean bean = (ElectionPeriodBean) source;

	List<CurricularYear> curricularYearsSet = new ArrayList<CurricularYear>();

	if (bean.getDegreeType() != null) {
	    for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
		curricularYearsSet.add(CurricularYear.readByYear(i));
	    }
	} else {
	    curricularYearsSet.addAll(RootDomainObject.getInstance().getCurricularYears());
	}

	Collections.sort(curricularYearsSet, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);

	return curricularYearsSet;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
