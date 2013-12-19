package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentDataBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MobilityProgramProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        MobilityStudentDataBean bean = (MobilityStudentDataBean) source;
        Set<MobilityProgram> mobilityPrograms =
                new TreeSet<MobilityProgram>(MobilityProgram.COMPARATOR_BY_REGISTRATION_AGREEMENT);
        for (MobilityAgreement agreement : Bennu.getInstance().getMobilityAgreementsSet()) {
            if (agreement.getUniversityUnit() == bean.getSelectedUniversity()) {
                mobilityPrograms.add(agreement.getMobilityProgram());
            }
        }
        return mobilityPrograms;
    }
}
