/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class PersistentObjectOJBReader extends PersistentObjectOJB {

	public IStudentCurricularPlan readStudentCurricularPlan(Integer studentNumber) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student.number", studentNumber);
		List result = query(StudentCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	public IStudent readStudent(Integer studentNumber, TipoCurso degreeType) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", studentNumber);
		criteria.addEqualTo("degreeType", degreeType);
		List result = query(Student.class, criteria);
		if (result.size() == 1) {
			return (IStudent) result.get(0);
		} else {
			return null;
		}
	}

	public IDegreeCurricularPlan readDegreeCurricularPlan(Integer degreeID) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degree.codigoInterno", degreeID);
		List result = query(DegreeCurricularPlan.class, criteria);
		if (result.size() == 1) {
			return (IDegreeCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	public ICurricularCourse readCurricularCourse(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("code", code);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} //else if (result.size() > 1) {
			//System.out.println("code: " + code + " result.size=" + result.size());
		//} else /* if (result.size() == 0) */ {
		//	System.out.println("code: " + code + " result.size=" + result.size());
		//}
		return null;
	}

	public ICurricularCourse readCurricularCourse(String name, Integer degreeID) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("name", name);
		criteria.addEqualTo("degreeCurricularPlan", degreeID);
		List result = query(CurricularCourse.class, criteria);
		if (result.size() == 1) {
			return (ICurricularCourse) result.get(0);
		} else if (result.size() > 1) {
			System.out.println("name: " + name + "  degreeId: " + degreeID + "  found: " + result.size());
		} else /* if (result.size() == 0) */ {
			System.out.println("name: " + name + "  degreeId: " + degreeID + "  found: " + result.size());
		}
		return null;
	}

	public Almeida_disc readAlmeidaCurricularCourse(String code) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("coddis", code);
		List result = query(Almeida_disc.class, criteria);
		//System.out.println("result.size" + result.size());
		if (result.size() > 0) {
			return (Almeida_disc) result.get(0);
		} else {
			return null;
		}
	}

}
