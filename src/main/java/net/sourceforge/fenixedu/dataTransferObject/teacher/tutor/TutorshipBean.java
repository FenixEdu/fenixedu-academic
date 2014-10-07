package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;

public class TutorshipBean implements Serializable {

    public static final Comparator<TutorshipBean> TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER = new BeanComparator(
            "tutorship.studentCurricularPlan.registration.number");

    private Tutorship tutorship;

    public TutorshipBean(Tutorship tutorship) {
        setTutorship(tutorship);
    }

    public Tutorship getTutorship() {
        return tutorship;
    }

    public void setTutorship(Tutorship tutorship) {
        this.tutorship = tutorship;
    }

    public Boolean getIsDislocatedFromPermanentResidence() {
        Registration registration = getTutorship().getStudentCurricularPlan().getRegistration();
        PersonalIngressionData personalIngressionData =
                registration.getStudent().getPersonalIngressionDataByExecutionYear(
                        registration.getStudentCandidacy().getExecutionYear());
        return personalIngressionData != null ? personalIngressionData.getDislocatedFromPermanentResidence() : null;
    }
}
