package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class GrantOwnerTypesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        if (source instanceof PersonalInformationBean) {
            PersonalInformationBean personalInformationBean = (PersonalInformationBean) source;
            Student student = personalInformationBean.getStudent();
            boolean isThirdCycle = false;
            boolean isFirstOrSecondCycle = false;

            isThirdCycle = student.hasActivePhdProgramProcess();
            for (Registration registration : student.getActiveRegistrations()) {
                isThirdCycle |= registration.isDEA();
                isFirstOrSecondCycle |= registration.isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
                isFirstOrSecondCycle |= registration.isMasterDegreeOrBolonhaMasterDegree();
            }
            if (isThirdCycle) {
                return GrantOwnerType.getTypesForPhDStudent();
            } else if (isFirstOrSecondCycle) {
                return GrantOwnerType.getTypesForFirstOrSecondCycle();
            }
        }
        return Arrays.asList(GrantOwnerType.values());
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
