package ServidorAplicacao.Servico.enrolment.degree.depercated;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 * 
 * 9/Abr/2003
 */
public class ShowAvailableCurricularCourses implements IService
{

    /**
	 * The actor of this class.
	 */
    public ShowAvailableCurricularCourses()
    {
    }

    /**
	 * @param infoStudent
	 * @param infoDegree
	 * @return EnrolmentContext
	 * @throws FenixServiceException
	 */
    public InfoEnrolmentContext run(IUserView userView) throws FenixServiceException
    {

        try
        {

            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
            IStudent student = studentDAO.readByUsername(((UserView) userView).getUtilizador());

            IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
////            IEnrolmentStrategy strategy =
////                enrolmentStrategyFactory.getEnrolmentStrategyInstance(
////                    EnrolmentContextManager.initialEnrolmentContext(student));
////
////            EnrolmentContext enrolmentContext = strategy.getAvailableCurricularCourses();
//
//            return EnrolmentContextManager.getInfoEnrolmentContext(
//                updateActualEnrolment(enrolmentContext));

            return null;
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace(System.out);
            throw new FenixServiceException(ex);
        }
        catch (IllegalStateException ex)
        {
            ex.printStackTrace(System.out);
            throw new FenixServiceException(ex);
        }
//        catch (OutOfCurricularCourseEnrolmentPeriod e)
//        {
//            throw e;
//        }
    }

    private EnrolmentContext updateActualEnrolment(EnrolmentContext enrolmentContext)
        throws ExcepcaoPersistencia
    {

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

        IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

        final List temporarilyEnrolments =
            persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
                enrolmentContext.getStudentActiveCurricularPlan(),
                EnrolmentState.TEMPORARILY_ENROLED);

        final List enrolmentsInOptionalCurricularCourses =
            (List) CollectionUtils.select(temporarilyEnrolments, new Predicate()
        {
            public boolean evaluate(Object obj)
            {
                if (obj instanceof IEnrolmentInOptionalCurricularCourse)
                {
                    IEnrolmentInOptionalCurricularCourse enrolment =
                        (IEnrolmentInOptionalCurricularCourse) obj;
                    return enrolment.getCurricularCourse().getType().equals(
                        new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
                }
                else
                {
                    return false;
                }
            }
        });

        final List enrolmentsNotInOptionalCurricularCourses =
            (List) CollectionUtils.subtract(
                temporarilyEnrolments,
                enrolmentsInOptionalCurricularCourses);

        List notOptionalCurricularCoursesTemporarilyEnroled =
            (List) CollectionUtils.collect(enrolmentsNotInOptionalCurricularCourses, new Transformer()
        {
            public Object transform(Object obj)
            {
                IEnrolment enrolment = (IEnrolment) obj;
                return (enrolment.getCurricularCourse());
            }
        });

        Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
        List noOptionalCourses = new ArrayList();
        while (iterator.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if (notOptionalCurricularCoursesTemporarilyEnroled.contains(curricularCourse))
            {
                noOptionalCourses.add(curricularCourse);
            }
        }
        enrolmentContext.setActualEnrolments(noOptionalCourses);
        enrolmentContext.getActualEnrolments().addAll(
            enrolmentContext.getCurricularCoursesAutomaticalyEnroled());
        enrolmentContext.setOptionalCurricularCoursesEnrolments(enrolmentsInOptionalCurricularCourses);

        return enrolmentContext;
    }
}