/*
 * StudentOJB.java
 *
 * Created on 28 December 2002, 17:19
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Ricardo Nortadas
 */

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

public class StudentOJB extends ObjectFenixOJB implements IPersistentStudent {

	/**
	 * @deprecated
	 */    
	public IStudent readByNumeroAndEstado(Integer numero, Integer estado, TipoCurso degreeType) throws ExcepcaoPersistencia {
		try {
			IStudent aluno = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where number = $1 and state = $2 and degreeType = $3";
			query.create(oqlQuery);
			query.bind(numero);
			query.bind(estado);
			query.bind(degreeType.getTipoCurso());
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				aluno = (IStudent) result.get(0);
			return aluno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IStudent readByUsername(String username) throws ExcepcaoPersistencia {
		try {
			IStudent student = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where person.username = $1";
			query.create(oqlQuery);
			query.bind(username);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				student = (IStudent) result.get(0);
			return student;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IStudent readByNumero(Integer numero, TipoCurso degreeType) throws ExcepcaoPersistencia {
		try {
			IStudent aluno = null;
			String oqlQuery = "select aluno from " + Student.class.getName();
			oqlQuery += " where number = $1 and degreeType = $2";
			query.create(oqlQuery);
			//query.bind(.getNome());
			query.bind(numero);
			query.bind(degreeType.getTipoCurso());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				aluno = (IStudent) result.get(0);
			return aluno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

//	public void lockWrite(IStudent aluno) throws ExcepcaoPersistencia, ExistingPersistentException {
//		IStudent studentInDB = null;
//		if (aluno == null)
//			return;
//
//		studentInDB = this.readByNumero(aluno.getNumber(), aluno.getDegreeType());
//
//		if (studentInDB == null)
//			super.lockWrite(aluno);
//		else if ((aluno instanceof Student) && ((Student) studentInDB).getInternalCode().equals(((Student) aluno).getInternalCode())) {
//			super.lockWrite(aluno);
//		} else
//			throw new ExistingPersistentException();
//	}
//
//	public void delete(IStudent student) throws ExcepcaoPersistencia {
//		// Delete all Attends
//		//		try {
//		//			String oqlQuery = "select all from " + Frequenta.class.getName();
//		//			oqlQuery += " where aluno.number = $1"
//		//			+ " and aluno.degreeType = $2";
//		//			query.create(oqlQuery);
//		//			query.bind(student.getNumber());
//		//			query.bind(student.getDegreeType());
//		//			List result = (List) query.execute();
//		//			ListIterator iterator = result.listIterator();
//		//			while(iterator.hasNext())
//		//				SuportePersistenteOJB.getInstance().getIFrequentaPersistente().delete((IFrequenta) iterator.next());
//		//		} catch (QueryException ex) {
//		//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		//		}
//
//		// Delete all Student Curricular Plans
//		try {
//			String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
//			oqlQuery += " where student.number = $1" + " and student.degreeType = $2";
//			query.create(oqlQuery);
//			query.bind(student.getNumber());
//			query.bind(student.getDegreeType());
//			List result = (List) query.execute();
//			ListIterator iterator = result.listIterator();
//			while (iterator.hasNext())
//				SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente().delete((IStudentCurricularPlan) iterator.next());
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
//
//		// Delete Student
//		super.delete(student);
//	}
//
//	/*    
//	     public void deleteByNumeroAndEstado(Integer numero, Integer estado) throws ExcepcaoPersistencia {
//	        try {
//	            String oqlQuery = "select all from " + Student.class.getName();
//	            oqlQuery += " where numero = $1 and estado = $2";
//	            query.create(oqlQuery);
//	            query.bind(numero);
//	            query.bind(estado);
//	            List result = (List) query.execute();
//	            ListIterator iterator = result.listIterator();
//	            while(iterator.hasNext())
//	                super.delete(iterator.next());
//	        } catch (QueryException ex) {
//	            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//	        }
//	    }
//	*/
//
//	public void deleteAll() throws ExcepcaoPersistencia {
//		try {
//			String oqlQuery = "select all from " + Student.class.getName();
//			query.create(oqlQuery);
//			List result = (List) query.execute();
//			Iterator iterator = result.iterator();
//			while (iterator.hasNext()) {
//				delete((IStudent) iterator.next());
//			}
//		} catch (QueryException ex) {
//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//		}
//
//	}

	/**
	 * @deprecated
	 */
	public IStudent readByNumeroAndEstadoAndPessoa(Integer numero, Integer estado, IPessoa pessoa, TipoCurso degreeType)
		throws ExcepcaoPersistencia {
		try {
			IStudent aluno = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where number = $1 and state = $2 and person.numeroDocumentoIdentificacao = $3";
			oqlQuery += " and degreeType = $4";

			query.create(oqlQuery);
			query.bind(numero);
			query.bind(estado);
			//query.bind(pessoa.getNome());
			query.bind(pessoa.getNumeroDocumentoIdentificacao());
			query.bind(degreeType.getTipoCurso());
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				aluno = (IStudent) result.get(0);
			return aluno;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

//	---------------------------------------------------------------------------------------------------------


	// feitos por David \ Ricardo
	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Student.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void delete(IStudent student) throws ExcepcaoPersistencia {
		try {
			super.delete(student);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public IStudent readStudentByDegreeTypeAndPerson(TipoCurso degreeType, IPessoa person) throws ExcepcaoPersistencia {
		try {
			IStudent student = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where degreeType  = $1";
			oqlQuery += " and person.username = $2";
			oqlQuery += " and person.numeroDocumentoIdentificacao = $3";
			oqlQuery += " and person.tipoDocumentoIdentificacao = $4";
			query.create(oqlQuery);
			query.bind(degreeType);
			query.bind(person.getUsername());
			query.bind(person.getNumeroDocumentoIdentificacao());			
			query.bind(person.getTipoDocumentoIdentificacao());
			
			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				student = (IStudent) result.get(0);
			}
			return student;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia {
		try {
			IStudent student = null;
			String oqlQuery = "select all from " + Student.class.getName();
			oqlQuery += " where number = $1";
			oqlQuery += " and degreeType = $2";
			query.create(oqlQuery);
			query.bind(number);
			query.bind(degreeType);

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				student = (IStudent) result.get(0);
			}
			return student;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	public void lockWrite(IStudent studentToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {
		IStudent studentFromDB = null;

		// If there is nothing to write, simply return.
		if (studentToWrite == null) {
			return;
		}

		// Read Student from database.
		studentFromDB = this.readStudentByNumberAndDegreeType(studentToWrite.getNumber(), studentToWrite.getDegreeType());

		// If Student is not in database, then write it.
		if (studentFromDB == null) {
			super.lockWrite(studentToWrite);
			// else If the CurricularYear is mapped to the database, then write any existing changes.
		} else if (
			(studentToWrite instanceof Student)
				&& ((Student) studentFromDB).getInternalCode().equals(((Student) studentToWrite).getInternalCode())) {
			super.lockWrite(studentToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();

	}

	public ArrayList readAll() throws ExcepcaoPersistencia {
		try {
			ArrayList listade = new ArrayList();
			String oqlQuery = "select all from " + Student.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					listade.add((IStudent) iterator.next());
			}
			return listade;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}
