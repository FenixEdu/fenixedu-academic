package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
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
public class ReadClassesForCurrentAndNextPeriodByDegree implements IService {

    public Object run(Integer degreeOID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
        ITurmaPersistente persistentClass = persistentSupport.getITurmaPersistente();

        IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod
                .readActualExecutionPeriod();
        IExecutionPeriod nextExecutionPeriod = getNextExecutionPeriod(persistentSupport, currentExecutionPeriod);

        IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeOID);

        List classes = persistentClass.readAll();

        return constructViews(classes, degree, currentExecutionPeriod, nextExecutionPeriod);
    }

    private IExecutionPeriod getNextExecutionPeriod(ISuportePersistente persistentSupport, IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia {
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
        List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

        for (Iterator iterator = executionPeriods.iterator(); iterator.hasNext(); ) {
            IExecutionPeriod otherExecutionPeriod = (IExecutionPeriod) iterator.next();
            if (otherExecutionPeriod.getPreviousExecutionPeriod() != null
                    && currentExecutionPeriod.getIdInternal().equals(otherExecutionPeriod.getPreviousExecutionPeriod().getIdInternal())) {
                return otherExecutionPeriod;
            }
        }

        return null;
    }

    private Object constructViews(List classes, final IDegree degree, final IExecutionPeriod currentExecutionPeriod, final IExecutionPeriod nextExecutionPeriod) {
        List classViews = new ArrayList();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            ISchoolClass klass = (ISchoolClass) iterator.next();
            if (isInPeriodsAndForDegree(klass, degree, currentExecutionPeriod, nextExecutionPeriod)) {
                ClassView classView = new ClassView();
                classView.setClassName(klass.getName());
                classView.setClassOID(klass.getIdInternal());
                classView.setCurricularYear(klass.getCurricularYear());
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

    private boolean isInPeriodsAndForDegree(ISchoolClass klass, IDegree degree,
            IExecutionPeriod currentExecutionPeriod, IExecutionPeriod nextExecutionPeriod) {
        return (klass.getExecutionPeriod().getIdInternal()
                .equals(currentExecutionPeriod.getIdInternal()) || (nextExecutionPeriod != null && klass.getExecutionPeriod()
                .getIdInternal().equals(nextExecutionPeriod.getIdInternal())))
                && klass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getIdInternal().equals(
                        degree.getIdInternal());
    }

}