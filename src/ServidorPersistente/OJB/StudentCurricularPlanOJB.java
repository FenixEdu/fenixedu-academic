/*
 * StudentCurricularPlanOJB.java
 *
 * Created on 21 of December of 2002, 17:01
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public class StudentCurricularPlanOJB extends ObjectFenixOJB implements IStudentCurricularPlanPersistente {
    
    /** Creates a new instance of StudentCurricularPlanOJB */
    public StudentCurricularPlanOJB() {
    }
    
    public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber , TipoCurso degreeType ) throws ExcepcaoPersistencia {
        try {
            IStudentCurricularPlan studentCurricularPlan = null;
            String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
            oqlQuery += " where student.number = $1" ;
            oqlQuery += " and student.degreeType = $2";
            oqlQuery += " and currentState = $3" ;

            query.create(oqlQuery);
            query.bind(studentNumber);
            query.bind(degreeType);
            query.bind(new Integer(StudentCurricularPlanState.ACTIVE));
            
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0) 
                studentCurricularPlan = (IStudentCurricularPlan) result.get(0);
            return studentCurricularPlan;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

	// TODO : This method is not yet availabe through the StudentCurricularPlanPersistente interface.
	//        I wrote it to be used in the lockWrite method, but maby it should be made available to
	//        the aplication layer as well.
	// TODO : Write a test case for this method.
	public IStudentCurricularPlan readStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlanToRead)
		throws ExcepcaoPersistencia {
			try {
				IStudentCurricularPlan studentCurricularPlanFromDB = null;
				String oqlQuery =
					"select all from " + StudentCurricularPlan.class.getName();
				oqlQuery += " where student.number = $1";
				oqlQuery += " and student.degreeType = $2";
				oqlQuery += " and courseCurricularPlan.name = $3";
				oqlQuery += " and courseCurricularPlan.degree.nome = $4";

				query.create(oqlQuery);
				query.bind(studentCurricularPlanToRead.getStudent().getNumber());
				query.bind(studentCurricularPlanToRead.getStudent().getDegreeType());
				query.bind(studentCurricularPlanToRead.getCourseCurricularPlan().getName());
				query.bind(studentCurricularPlanToRead.getCourseCurricularPlan().getDegree().getNome());

				List result = (List) query.execute();
				lockRead(result);
				if (result.size() != 0)
					studentCurricularPlanFromDB = (IStudentCurricularPlan) result.get(0);
				return studentCurricularPlanFromDB;
			} catch (QueryException ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	}

	public void lockWrite(IStudentCurricularPlan studentCurricularPlanToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		IStudentCurricularPlan studentCurricularPlanFromDB = null;

		// If there is nothing to write, simply return.
		if (studentCurricularPlanToWrite == null)
			return;

		// Read studentCurricularPlan from database.
		studentCurricularPlanFromDB = this.readStudentCurricularPlan(studentCurricularPlanToWrite);

		// If studentCurricularPlan is not in database, then write it.
		if (studentCurricularPlanFromDB == null)
			super.lockWrite(studentCurricularPlanToWrite);
		// else If the studentCurricularPlan is mapped to the database, then write any existing changes.
		else if (
			(studentCurricularPlanToWrite instanceof StudentCurricularPlan)
				&& ((StudentCurricularPlan) studentCurricularPlanFromDB).getInternalCode().equals(
					((StudentCurricularPlan) studentCurricularPlanToWrite).getInternalCode())) {
			super.lockWrite(studentCurricularPlanToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}
    
    public void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia {
        super.delete(curricularPlan);
    }
    
     public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
        super.deleteAll(oqlQuery);
   }

    public List readAllFromStudent(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia {
        try {
           String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
            oqlQuery += " where student.number = " + studentNumber;

// ACRESCENTAR AQUI A VERIFICACAO DO TIPO DE ALUNO
            
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }   

}
