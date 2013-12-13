package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularYearsGivenDegreeTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ElectionPeriodBean bean = (ElectionPeriodBean) source;

        List<CurricularYear> curricularYearsSet = new ArrayList<CurricularYear>();

        if (bean.getDegreeType() != null) {
            for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
                curricularYearsSet.add(CurricularYear.readByYear(i));
            }
        } else {
            curricularYearsSet.addAll(Bennu.getInstance().getCurricularYearsSet());
        }

        Collections.sort(curricularYearsSet, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);

        return curricularYearsSet;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
