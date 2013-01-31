package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.AbstractPrescriptionRule;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class StudentsLowPerformancePrescriptionProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		List<PrescriptionEnum> prescriptionBeans = new LinkedList<PrescriptionEnum>();
		for (AbstractPrescriptionRule abstractPrescriptionRule : AbstractPrescriptionRule.readProviderPrescriptionRules()) {
			prescriptionBeans.add(abstractPrescriptionRule.getPrescriptionEnum());
		}
		return prescriptionBeans;
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}
}
