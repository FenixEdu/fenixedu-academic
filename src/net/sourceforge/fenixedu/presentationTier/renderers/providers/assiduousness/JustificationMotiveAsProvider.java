package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.RegularizationMonthFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class JustificationMotiveAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        CorrectionType correctionType = null;
        JustificationType justificationType = null;
        if (source instanceof EmployeeJustificationFactory) {
            EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) source;
            correctionType = employeeJustificationFactory.getCorrectionType();
            justificationType = employeeJustificationFactory.getJustificationType();
        } else if(source instanceof RegularizationMonthFactory){
            RegularizationMonthFactory regularizationMonthFactory = (RegularizationMonthFactory) source;
            correctionType = regularizationMonthFactory.getCorrectionType();
        }

        return getJustificationMotivesList(correctionType,justificationType);
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    private List<JustificationMotive> getJustificationMotivesList(CorrectionType correctionType,
            JustificationType justificationType) {
        List<JustificationMotive> justificationMotives = new ArrayList<JustificationMotive>();
        if (correctionType.equals(CorrectionType.JUSTIFICATION) && justificationType != null) {
            for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
                    .getJustificationMotives()) {
                if (justificationMotive.getJustificationType() != null
                        && justificationMotive.getJustificationType().equals(justificationType)) {
                    justificationMotives.add(justificationMotive);
                }
            }
        } else if (correctionType.equals(CorrectionType.REGULARIZATION)) {
            for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
                    .getJustificationMotives()) {
                if (justificationMotive.getJustificationType() == null) {
                    justificationMotives.add(justificationMotive);
                }
            }
        }
        Collections.sort(justificationMotives, new BeanComparator("acronym"));
        return justificationMotives;
    }
}
