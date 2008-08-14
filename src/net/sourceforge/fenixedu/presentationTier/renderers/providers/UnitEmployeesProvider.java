package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.assiduousnessStructure.AssiduousnessPersonFunctionFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnitEmployeesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof VigilantGroupBean) {
	    VigilantGroupBean bean = (VigilantGroupBean) source;
	    Unit unit = bean.getUnit();

	    ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	    return unit.getAllWorkingEmployees(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());
	} else if (source instanceof AssiduousnessPersonFunctionFactory) {
	    AssiduousnessPersonFunctionFactory assiduousnessPersonFunctionFactory = (AssiduousnessPersonFunctionFactory) source;
	    return ((Unit) assiduousnessPersonFunctionFactory.getParty()).getAllCurrentNonTeacherEmployees();
	}
	return null;

    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

}
