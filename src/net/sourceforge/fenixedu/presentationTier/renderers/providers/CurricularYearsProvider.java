package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurricularYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        List<CurricularYear> curricularYearsSet = new ArrayList<CurricularYear>();
        curricularYearsSet.addAll(RootDomainObject.getInstance().getCurricularYears());
        Collections.sort(curricularYearsSet, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);
        return curricularYearsSet;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();  
    }

}
