package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ClassView;
import Dominio.Degree;
import Dominio.IDegree;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndNextPeriodByDegree implements IService {

    public Object run(Integer degreeOID) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
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
                classView.setClassName(klass.getNome());
                classView.setClassOID(klass.getIdInternal());
                classView.setCurricularYear(klass.getAnoCurricular());
                classView.setSemester(klass.getExecutionPeriod().getSemester());
                classView.setDegreeCurricularPlanID(klass.getExecutionDegree().getCurricularPlan().getIdInternal());
                classView.setDegreeInitials(klass.getExecutionDegree().getCurricularPlan().getDegree().getSigla());
                classView.setNameDegreeCurricularPlan(klass.getExecutionDegree().getCurricularPlan().getName());
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
                && klass.getExecutionDegree().getCurricularPlan().getDegree().getIdInternal().equals(
                        degree.getIdInternal());
    }

}