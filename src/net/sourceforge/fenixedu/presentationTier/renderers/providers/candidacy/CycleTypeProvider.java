package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	if (source instanceof ExecutionDegreeBean) {
	    ExecutionDegreeBean executionDegreeBean = (ExecutionDegreeBean) source;
	    if (executionDegreeBean.getDegree() != null) {
		return executionDegreeBean.getDegree().getDegreeType().getCycleTypes();
	    }
	} else if (source instanceof StudentCurricularPlanCreator) {
	    StudentCurricularPlanCreator studentCurricularPlanCreator = (StudentCurricularPlanCreator) source;
	    if (studentCurricularPlanCreator.getDegree() != null) {
		return studentCurricularPlanCreator.getDegree().getDegreeType().getCycleTypes();
	    }
	}

	return new ArrayList<CycleType>();
    }

    public Converter getConverter() {
	return null;
    }

}
