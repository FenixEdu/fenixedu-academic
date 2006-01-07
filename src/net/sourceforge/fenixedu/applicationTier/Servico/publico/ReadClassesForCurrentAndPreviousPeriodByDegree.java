package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndPreviousPeriodByDegree implements IService {

    public Object run(Integer degreeOID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
        ITurmaPersistente persistentClass = persistentSupport.getITurmaPersistente();

        ExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod
                .readActualExecutionPeriod();
        ExecutionPeriod previouseExecutionPeriod = currentExecutionPeriod
                .getPreviousExecutionPeriod();

        Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeOID);

        List classes = persistentClass.readAll();

        return constructViews(classes, degree, currentExecutionPeriod, previouseExecutionPeriod);
    }

    private Object constructViews(List classes, final Degree degree, final ExecutionPeriod currentExecutionPeriod, final ExecutionPeriod previouseExecutionPeriod) {
        List classViews = new ArrayList();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            SchoolClass klass = (SchoolClass) iterator.next();
            if (isInPeriodsAndForDegree(klass, degree, currentExecutionPeriod,
                    previouseExecutionPeriod)) {
                ClassView classView = new ClassView();
                classView.setClassName(klass.getNome());
                classView.setClassOID(klass.getIdInternal());
                classView.setCurricularYear(klass.getAnoCurricular());
                classView.setSemester(klass.getExecutionPeriod().getSemester());
                classView.setDegreeCurricularPlanID(klass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
                classView.setDegreeInitials(klass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla());
                classView.setNameDegreeCurricularPlan(klass.getExecutionDegree().getDegreeCurricularPlan().getName());
                classView.setExecutionPeriodOID(klass.getExecutionPeriod().getIdInternal());
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