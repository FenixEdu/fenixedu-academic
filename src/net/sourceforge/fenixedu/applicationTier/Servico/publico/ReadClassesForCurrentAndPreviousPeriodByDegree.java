package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndPreviousPeriodByDegree extends Service {

    public Object run(Integer degreeOID) throws ExcepcaoPersistencia {
        ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        ExecutionPeriod previouseExecutionPeriod = currentExecutionPeriod
                .getPreviousExecutionPeriod();

        Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

        List classes = rootDomainObject.getSchoolClasss();

        return constructViews(classes, degree, currentExecutionPeriod, previouseExecutionPeriod);
    }

    private Object constructViews(List classes, final Degree degree, final ExecutionPeriod currentExecutionPeriod, final ExecutionPeriod previouseExecutionPeriod) {
        List classViews = new ArrayList();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            SchoolClass klass = (SchoolClass) iterator.next();
            if (isInPeriodsAndForDegree(klass, degree, currentExecutionPeriod,
                    previouseExecutionPeriod)) {
                ClassView classView = new ClassView(klass);
                classViews.add(classView);
            }
        }
        return classViews;
    }

    private boolean isInPeriodsAndForDegree(SchoolClass klass, Degree degree,
            ExecutionPeriod currentExecutionPeriod, ExecutionPeriod previouseExecutionPeriod) {
        return (klass.getExecutionPeriod().getIdInternal()
                .equals(currentExecutionPeriod.getIdInternal()) || klass.getExecutionPeriod()
                .getIdInternal().equals(previouseExecutionPeriod.getIdInternal()))
                && klass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getIdInternal().equals(
                        degree.getIdInternal());
    }

}