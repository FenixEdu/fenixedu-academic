/*
 * StudentOJB.java
 * 
 * Created on 28 December 2002, 17:19
 */

package ServidorPersistente.OJB;

/**
 * @author Ricardo Nortadas
 */

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

public class StudentOJB extends ObjectFenixOJB implements IPersistentStudent
{

    /**
	 * @deprecated
	 */
    public IStudent readByNumeroAndEstado(Integer numero, Integer estado, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        try
        {
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
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IStudent readByUsername(String username) throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", username);

        IStudent student = (IStudent) queryObject(Student.class, crit);

        return student;

    }

    public IStudent readByNumero(Integer numero, TipoCurso degreeType) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", numero);
        criteria.addEqualTo("degreeType", degreeType);

        return (IStudent) queryObject(Student.class, criteria);
    }

    //
    //	public void delete(IStudent student) throws ExcepcaoPersistencia {
    //		// Delete all Attends
    //		// try {
    //		// String oqlQuery = "select all from " + Frequenta.class.getName();
    //		// oqlQuery += " where aluno.number = $1"
    //		// + " and aluno.degreeType = $2";
    //		// query.create(oqlQuery);
    //		// query.bind(student.getNumber());
    //		// query.bind(student.getDegreeType());
    //		// List result = (List) query.execute();
    //		// ListIterator iterator = result.listIterator();
    //		// while(iterator.hasNext())
    //		// SuportePersistenteOJB.getInstance().getIFrequentaPersistente().delete((IFrequenta)
    // iterator.next());
    //		// } catch (QueryException ex) {
    //		// throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
    //		// }
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
    //				SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente().delete((IStudentCurricularPlan)
    // iterator.next());
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
    public IStudent readByNumeroAndEstadoAndPessoa(
        Integer numero,
        Integer estado,
        IPessoa pessoa,
        TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        try
        {
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
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    //	---------------------------------------------------------------------------------------------------------

    // feitos por David \ Ricardo
    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Student.class.getName();
            super.deleteAll(oqlQuery);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public void delete(IStudent student) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(student);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public IStudent readStudentByDegreeTypeAndPerson(TipoCurso degreeType, IPessoa person)
        throws ExcepcaoPersistencia
    {
        try
        {
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
            try
            {
                lockRead(result);
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw ex;
            }

            if ((result != null) && (result.size() != 0))
            {
                student = (IStudent) result.get(0);
            }
            return student;

        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("degreeType", degreeType);

        return (IStudent) queryObject(Student.class, criteria);
    }

    public void lockWrite(IStudent studentToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IStudent studentFromDB = null;

        // If there is nothing to write, simply return.
        if (studentToWrite == null)
        {
            return;
        }

        // Read Student from database.
        studentFromDB =
            this.readStudentByNumberAndDegreeType(
                studentToWrite.getNumber(),
                studentToWrite.getDegreeType());

        // If Student is not in database, then write it.
        if (studentFromDB == null)
        {
            super.lockWrite(studentToWrite);
            // else If the CurricularYear is mapped to the database, then write any existing changes.
        }
        else if (
            (studentToWrite instanceof Student)
                && ((Student) studentFromDB).getIdInternal().equals(
                    ((Student) studentToWrite).getIdInternal()))
        {
            super.lockWrite(studentToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + Student.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public IStudent readByPersonAndDegreeType(IPessoa person, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        try
        {
            IStudent student = null;
            String oqlQuery =
                "select all from "
                    + Student.class.getName()
                    + " where person.idInternal = $1"
                    + " and degreeType = $2";
            query.create(oqlQuery);
            query.bind(person.getIdInternal());
            query.bind(degreeType);

            List result = (List) query.execute();
            try
            {
                lockRead(result);
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw ex;
            }

            if ((result != null) && (result.size() != 0))
            {
                student = (IStudent) result.get(0);
            }
            return student;

        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }

    }

    public Integer generateStudentNumber(TipoCurso degreeType) throws ExcepcaoPersistencia
    {
        try
        {
            Integer number = new Integer(0);

            String oqlQuery =
                "select all from "
                    + Student.class.getName()
                    + " where degreeType = $1"
                    + " order by number desc";
            query.create(oqlQuery);

            query.bind(degreeType);

            List result = (List) query.execute();
            try
            {
                lockRead(result);
            }
            catch (ExcepcaoPersistencia ex)
            {
                throw ex;
            }

            if ((result != null) && (result.size() != 0))
            {
                number = ((IStudent) result.get(0)).getNumber();
            }

            // FIXME: ISTO E UMA SOLUCAO TEMPORARIA DEVIDO A EXISTIREM ALUNOS NA SECRETARIA QUE
            // POR UM MOTIVO OU OUTRO NAO SE ENCONTRAM NA BASE DE DADOS

            if (degreeType.equals(TipoCurso.MESTRADO_OBJ) && (number.intValue() < 5411))
            {
                number = new Integer(5411);
            }

            return new Integer(number.intValue() + 1);

        }
        catch (QueryException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readbyPerson(IPessoa person) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("personKey", person.getIdInternal());

        return queryList(Student.class, criteria);
    }

    public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(
        String studentName,
        String idNumber,
        TipoDocumentoIdentificacao idType,
        Integer studentNumber)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        if (studentName != null)
        {
            criteria.addLike("person.nome", studentName);
        }

        if (idNumber != null)
        {
            criteria.addEqualTo("person.numeroDocumentoIdentificacao", idNumber);
        }

        if (idType != null)
        {
            criteria.addEqualTo("person.tipoDocumentoIdentificacao", idType.getTipo());
        }

        if (studentNumber != null)
        {
            criteria.addEqualTo("number", studentNumber);
        }

        criteria.addEqualTo("degreeType", TipoCurso.MESTRADO_OBJ.getTipoCurso());

        return queryList(Student.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#readAllBySpan(java.lang.Integer,
	 *      java.lang.Integer)
	 */
    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return readSpan(Student.class, criteria, numberOfElementsInSpan, spanNumber);
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWAluno#countAll()
	 */
    public Integer countAll()
    {
        return new Integer(count(Student.class, new Criteria()));
    }

}