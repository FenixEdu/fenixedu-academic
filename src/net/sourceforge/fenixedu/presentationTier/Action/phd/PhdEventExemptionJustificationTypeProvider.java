package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemptionJustificationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PhdEventExemptionJustificationTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        ArrayList<PhdEventExemptionJustificationType> list = new ArrayList<PhdEventExemptionJustificationType>();
        for (PhdEventExemptionJustificationType type : PhdEventExemptionJustificationType.values()) {
            if (!type.equals(PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION)) {
                list.add(type);
            }
        }
        return list;
    }

}
