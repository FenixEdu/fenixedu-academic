/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.OJB.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ProfessionalCareer;
import Dominio.teacher.TeachingCareer;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerOJB extends ObjectFenixOJB implements IPersistentCareer {

	/**
	 *  
	 */
	public CareerOJB() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCareer#readAllByTeacher(Dominio.ITeacher)
	 */
	public List readAllByTeacher(ITeacher teacher)
		throws ExcepcaoPersistencia {

		List careers = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		careers = queryList(Career.class, criteria);

		return careers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCareer#readAllProfessionalCareersByTeacher(Dominio.ITeacher)
	 */
	public List readAllProfessionalCareersByTeacher(ITeacher teacher)
		throws ExcepcaoPersistencia {

		List professionalCareers = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		criteria.addEqualTo(
			"ojbConcreteClass",
			ProfessionalCareer.class.getName());
		professionalCareers = queryList(Career.class, criteria);

		return professionalCareers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCareer#readAllTeachingCareerByTeacher(Dominio.ITeacher)
	 */
	public List readAllTeachingCareerByTeacher(ITeacher teacher)
		throws ExcepcaoPersistencia {
		
			List teachingCareers = new ArrayList();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
			criteria.addEqualTo(
				"ojbConcreteClass",
				TeachingCareer.class.getName());
			teachingCareers = queryList(Career.class, criteria);

			return teachingCareers;
	}
}
