package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndNextPeriodByDegree extends Service {

    public Object run(final Integer degreeOID) throws ExcepcaoPersistencia {
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();

        final ExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        final ExecutionPeriod nextExecutionPeriod = currentExecutionPeriod.getNextExecutionPeriod();

        final Degree degree = (Degree) persistentObject.readByOID(Degree.class, degreeOID);

        final int numClassesCurrentPeriod = currentExecutionPeriod.getSchoolClasses().size();
        final int numClassesNextPeriod = nextExecutionPeriod.getSchoolClasses().size();

        final List classViews = new ArrayList(numClassesCurrentPeriod + numClassesNextPeriod);
        constructViews(classViews, degree, currentExecutionPeriod);
        constructViews(classViews, degree, nextExecutionPeriod);

        return classViews;
    }

    private void constructViews(final List classViews, final Degree degree,
            final ExecutionPeriod executionPeriod) {
        for (final SchoolClass schoolClass : executionPeriod.getSchoolClasses()) {
            if (isForDegree(schoolClass, degree)) {
                ClassView classView = new ClassView();
                classView.setClassName(schoolClass.getNome());
                classView.setClassOID(schoolClass.getIdInternal());
                classView.setCurricularYear(schoolClass.getAnoCurricular());
                classView.setSemester(schoolClass.getExecutionPeriod().getSemester());
                classView.setDegreeCurricularPlanID(schoolClass.getExecutionDegree().getDegreeCurricularPlan()
                        .getIdInternal());
                classView.setDegreeInitials(schoolClass.getExecutionDegree().getDegreeCurricularPlan()
                        .getDegree().getSigla());
                classView.setNameDegreeCurricularPlan(schoolClass.getExecutionDegree()
                        .getDegreeCurricularPlan().getName());
                classView.setExecutionPeriodOID(schoolClass.getExecutionPeriod().getIdInternal());
                classViews.add(classView);
            }
        }
    }

    private boolean isForDegree(final SchoolClass schoolClass, final Degree degree) {
        final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final Degree degreeFromSchoolClass = degreeCurricularPlan.getDegree();
        return degreeFromSchoolClass.getIdInternal().equals(degree.getIdInternal());
    }

}