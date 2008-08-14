package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularYearsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	Integer index = null;

	if (source instanceof ImportContentBean) {
	    ImportContentBean bean = (ImportContentBean) source;
	    index = bean.getExecutionDegree() != null ? bean.getExecutionDegree().getDegree().getDegreeType().getYears() : null;
	}

	List<CurricularYear> curricularYearsSet = new ArrayList<CurricularYear>();
	curricularYearsSet.addAll(RootDomainObject.getInstance().getCurricularYears());
	Collections.sort(curricularYearsSet, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);
	return index == null ? curricularYearsSet : curricularYearsSet.subList(0, index);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
