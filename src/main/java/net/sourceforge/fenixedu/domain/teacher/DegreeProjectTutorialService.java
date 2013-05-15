package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.credits.util.ProjectTutorialServiceBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class DegreeProjectTutorialService extends DegreeProjectTutorialService_Base {

    public DegreeProjectTutorialService(Professorship professorship, Attends attend, Integer percentageValue) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setProfessorship(professorship);
        TeacherService teacherService =
                TeacherService.getTeacherService(professorship.getTeacher(), professorship.getExecutionCourse()
                        .getExecutionYear().getNextExecutionYear().getFirstExecutionPeriod());
        setTeacherService(teacherService);
        setAttend(attend);
        setPercentageValue(percentageValue);
    }

    @Override
    public void setPercentageValue(Integer percentageValue) {
        checkPercentage(percentageValue);
        super.setPercentageValue(percentageValue);
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.degreeProjectTutorialService.change", getProfessorship()
                        .getExecutionCourse().getName(), getProfessorship().getTeacher().getPerson().getNickname(), getAttend()
                        .getRegistration().getNumber().toString(), getPercentageValue().toString()));
    }

    private void checkPercentage(Integer percentageValue) {
        Integer availablePercentage = new Integer(100);
        if (percentageValue == null || percentageValue.compareTo(0) < 0 || percentageValue.compareTo(availablePercentage) > 0) {
            throw new DomainException("message.invalid.percentage");
        }
        for (DegreeProjectTutorialService degreeProjectTutorialService : getAttend().getDegreeProjectTutorialServices()) {
            if (!degreeProjectTutorialService.equals(this)) {
                availablePercentage = availablePercentage - degreeProjectTutorialService.getPercentageValue();
            }
        }
        if (percentageValue.compareTo(availablePercentage) > 0) {
            throw new DomainException("message.exceeded.percentage");
        }
    }

    @Service
    public static void updateProjectTutorialService(List<ProjectTutorialServiceBean> projectTutorialServicesBean) {
        for (ProjectTutorialServiceBean projectTutorialServiceBean : projectTutorialServicesBean) {
            if (!projectTutorialServiceBean.getProfessorship().getExecutionCourse().getProjectTutorialCourse()) {
                throw new DomainException("message.invalid.executionCourseType");
            }
            DegreeProjectTutorialService degreeProjectTutorialService =
                    projectTutorialServiceBean.getDegreeProjectTutorialService();
            if (projectTutorialServiceBean.getPercentage() == null) {
                projectTutorialServiceBean.setPercentage(0);
            }
            if (degreeProjectTutorialService == null && projectTutorialServiceBean.getPercentage() != 0) {
                degreeProjectTutorialService =
                        new DegreeProjectTutorialService(projectTutorialServiceBean.getProfessorship(),
                                projectTutorialServiceBean.getAttend(), projectTutorialServiceBean.getPercentage());

            } else if (degreeProjectTutorialService != null
                    && !degreeProjectTutorialService.getPercentageValue().equals(projectTutorialServiceBean.getPercentage())) {
                degreeProjectTutorialService.setPercentageValue(projectTutorialServiceBean.getPercentage());
            }
        }
    }

    @Override
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.degreeProjectTutorialService.delete", getProfessorship()
                        .getExecutionCourse().getName(), getProfessorship().getTeacher().getPerson().getNickname(), getAttend()
                        .getRegistration().getNumber().toString(), getPercentageValue().toString()));
        removeAttend();
        removeProfessorship();
        super.delete();
    }

    public BigDecimal getDegreeProjectTutorialServiceCredits() {
        if (getAttend().getEnrolment() != null && getAttend().getEnrolment().isApproved()) {
            return new BigDecimal((((double) getPercentageValue()) / 100)
                    * (getAttend().getExecutionCourse().getEctsCredits() / 60)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
