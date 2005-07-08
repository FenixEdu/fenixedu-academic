package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndNextPeriodByDegree implements IService {

    public Object run(final Integer degreeOID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        final ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();

        final IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        final IExecutionPeriod nextExecutionPeriod = currentExecutionPeriod.getNextExecutionPeriod();

        final IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeOID);

        final int numClassesCurrentPeriod = currentExecutionPeriod.getSchoolClasses().size();
        final int numClassesNextPeriod = nextExecutionPeriod.getSchoolClasses().size();

        final List classViews = new ArrayList(numClassesCurrentPeriod + numClassesNextPeriod);
        constructViews(classViews, degree, currentExecutionPeriod);
        constructViews(classViews, degree, nextExecutionPeriod);

        return classViews;
    }

    private void constructViews(final List classViews, final IDegree degree,
            final IExecutionPeriod executionPeriod) {
        for (final ISchoolClass schoolClass : executionPeriod.getSchoolClasses()) {
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

    private boolean isForDegree(final ISchoolClass schoolClass, final IDegree degree) {
        final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
        final IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final IDegree degreeFromSchoolClass = degreeCurricularPlan.getDegree();
        return degreeFromSchoolClass.getIdInternal().equals(degree.getIdInternal());
    }

}