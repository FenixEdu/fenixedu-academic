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
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndPreviousPeriodByDegree implements IService {

    public Object run(Integer degreeOID) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport
                .getIPersistentDegreeCurricularPlan();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
        ITurmaPersistente persistentClass = persistentSupport.getITurmaPersistente();

        IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod
                .readActualExecutionPeriod();
        IExecutionPeriod previouseExecutionPeriod = currentExecutionPeriod
                .getPreviousExecutionPeriod();

        IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeOID);

        List classes = persistentClass.readAll();

        return constructViews(classes, degree, currentExecutionPeriod, previouseExecutionPeriod);
    }

    private Object constructViews(List classes, final IDegree degree, final IExecutionPeriod currentExecutionPeriod, final IExecutionPeriod previouseExecutionPeriod) {
        List classViews = new ArrayList();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            ISchoolClass klass = (ISchoolClass) iterator.next();
            if (isInPeriodsAndForDegree(klass, degree, currentExecutionPeriod,
                    previouseExecutionPeriod)) {
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
            IExecutionPeriod currentExecutionPeriod, IExecutionPeriod previouseExecutionPeriod) {
        return (klass.getExecutionPeriod().getIdInternal()
                .equals(currentExecutionPeriod.getIdInternal()) || klass.getExecutionPeriod()
                .getIdInternal().equals(previouseExecutionPeriod.getIdInternal()))
                && klass.getExecutionDegree().getCurricularPlan().getDegree().getIdInternal().equals(
                        degree.getIdInternal());
    }

}