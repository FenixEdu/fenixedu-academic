/*
 * Created on 31/Out/2003
 */
package middleware.ScopesClean;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourseScope;
import Dominio.Enrolment;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteAlterativeCurricularCourseScopes {

	public static void main(String[] args) {

		System.out.println("Running DeleteAlterativeCurricularCourseScopes script");
		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		ICurricularCourseScope ccs = null;

		// This will delete all the alternative curricular courses scopes
		// from all the curricular courses

		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(CurricularCourseScope.class, criteria);
		List curricularCourseScopeList = (List) broker.getCollectionByQuery(query);

		broker.clearCache();
		broker.beginTransaction();

		Iterator iterator = curricularCourseScopeList.iterator();
		int curricularSemesterKey = 0;
		int changedEnrolments = 0;
		int ccScopes = 0;

		try {

			while (iterator.hasNext()) {

				ccs = (CurricularCourseScope) iterator.next();

				criteria = new Criteria();
				query = null;

				//criteria.addEqualTo("key_curricular_course", ccs.getCurricularCourseKey());
				criteria.addEqualTo("curricularCourse.idInternal", ccs.getCurricularCourse().getIdInternal());
				criteria.addEqualTo("branch.idInternal", ccs.getBranch().getIdInternal());
				curricularSemesterKey = ccs.getCurricularSemester().getIdInternal().intValue() + 1;

				criteria.addEqualTo("curricularSemester.idInternal", new Integer(curricularSemesterKey));
				criteria.addEqualTo(
					"curricularCourse.degreeCurricularPlan.idInternal",
					ccs.getCurricularCourse().getDegreeCurricularPlan().getIdInternal());
				criteria.addLike("curricularCourse.degreeCurricularPlan.name", "%2003/2004");
				query = new QueryByCriteria(CurricularCourseScope.class, criteria);

				ICurricularCourseScope alternativeCCScope = (CurricularCourseScope) broker.getObjectByQuery(query);

				if (alternativeCCScope == null)
					continue;

				String curricularCourseName = alternativeCCScope.getCurricularCourse().getName().toUpperCase();

				if (curricularCourseName.equals("MECÂNICA E ONDAS")
					|| curricularCourseName.equals("PROPAGAÇÃO E RADIAÇÃO DE ONDAS ELECTROMAGNÉTICAS"))
					continue;

				ccScopes++;
				criteria = new Criteria();
				criteria.addEqualTo("curricularCourseScopeKey", alternativeCCScope.getIdInternal());
				query = new QueryByCriteria(Enrolment.class, criteria);

				List enrolmentList = (List) broker.getCollectionByQuery(query);

				broker.delete(alternativeCCScope);

				if ((enrolmentList != null) || (!enrolmentList.isEmpty())) {

					Iterator enrolmentIterator = enrolmentList.iterator();
					while (enrolmentIterator.hasNext()) {

						IEnrolment enrolment = (Enrolment) enrolmentIterator.next();

						if (enrolment != null) {

							changedEnrolments++;
							enrolment.setCurricularCourseScope(ccs);
							broker.store(enrolment);
						}
					}
				}
			}
		}
		catch (PersistenceBrokerException pbe) {
			System.out.println("A PersistenceBrokerException has occured");
			pbe.printStackTrace();
		}

		broker.commitTransaction();
		broker.close();

		System.out.println(changedEnrolments + " enrolments were changed from " + ccScopes + " scopes");

	}
}
