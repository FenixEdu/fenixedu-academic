package ServidorAplicacao.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class GetUserViewFromStudentNumberAndDegreeType implements IServico {

	private static GetUserViewFromStudentNumberAndDegreeType _servico = new GetUserViewFromStudentNumberAndDegreeType();

	public static GetUserViewFromStudentNumberAndDegreeType getService() {
		return _servico;
	}

	private GetUserViewFromStudentNumberAndDegreeType() {
	}

	public final String getNome() {
		return "GetUserViewFromStudentNumberAndDegreeType";
	}

	public IUserView run(Integer degreeTypeInt, Integer studentNumber) throws FenixServiceException {

		IUserView userView = null;
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
			TipoCurso degreeType = new TipoCurso(degreeTypeInt);
			IStudent student = studentDAO.readStudentByNumberAndDegreeType(studentNumber, degreeType);
			if(student != null) {
				userView = new UserView(student.getPerson().getUsername(), student.getPerson().getPersonRoles());
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return userView;
	}

	private EnrolmentContext updateActualEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		final List temporarilyEnrolments = persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(enrolmentContext.getStudentActiveCurricularPlan(), EnrolmentState.TEMPORARILY_ENROLED_OBJ);

		final List enrolmentsInOptionalCurricularCourses = (List) CollectionUtils.select(temporarilyEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				if (obj instanceof IEnrolmentInOptionalCurricularCourse) {
					IEnrolmentInOptionalCurricularCourse enrolment = (IEnrolmentInOptionalCurricularCourse) obj;
					return enrolment.getCurricularCourseScope().getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE));
				} else {
					return false;
				}
			}
		});

		final List enrolmentsNotInOptionalCurricularCourses = (List) CollectionUtils.subtract(temporarilyEnrolments, enrolmentsInOptionalCurricularCourses);

		List notOptionalCurricularCoursesTemporarilyEnroled = (List) CollectionUtils.collect(enrolmentsNotInOptionalCurricularCourses, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourseScope().getCurricularCourse());
			}
		});

		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		List noOptionalCourseScopes = new ArrayList();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (notOptionalCurricularCoursesTemporarilyEnroled.contains(curricularCourseScope.getCurricularCourse())) {
				noOptionalCourseScopes.add(curricularCourseScope);
			}
		}
		enrolmentContext.setActualEnrolments(noOptionalCourseScopes);
		enrolmentContext.getActualEnrolments().addAll(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled());
		enrolmentContext.setOptionalCurricularCoursesEnrolments(enrolmentsInOptionalCurricularCourses);

		return enrolmentContext;
	}
}