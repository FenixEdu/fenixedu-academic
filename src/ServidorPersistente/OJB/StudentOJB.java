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
        Criteria crit = new Criteria();
        crit.addEqualTo("number", numero);
        crit.addEqualTo("state", estado);
        crit.addEqualTo("degreeType", degreeType.getTipoCurso());
        return (IStudent) queryObject(Student.class, crit);

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

    //	---------------------------------------------------------------------------------------------------------

    // feitos por David \ Ricardo

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
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeType", degreeType);
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("person.numeroDocumentoIdentificacao", person.getNumeroDocumentoIdentificacao());
        crit.addEqualTo("person.tipoDocumentoIdentificacao", person.getTipoDocumentoIdentificacao());
        return (IStudent) queryObject(Student.class, crit);

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
            // else If the CurricularYear is mapped to the database, then write
            // any existing changes.
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
        return queryList(Student.class, new Criteria());
    }

    public IStudent readByPersonAndDegreeType(IPessoa person, TipoCurso degreeType)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        crit.addEqualTo("degreeType", degreeType);
        return (IStudent) queryObject(Student.class, crit);

    }

    public Integer generateStudentNumber(TipoCurso degreeType) throws ExcepcaoPersistencia
    {
        
            Integer number = new Integer(0);
            Criteria crit = new Criteria();
            crit.addEqualTo("degreeType", degreeType);
            crit.addOrderBy("number", false);
           
            List result = queryList(Student.class,crit);
           

            if ((result != null) && (result.size() != 0))
            {
                number = ((IStudent) result.get(0)).getNumber();
            }

            // FIXME: ISTO E UMA SOLUCAO TEMPORARIA DEVIDO A EXISTIREM ALUNOS
            // NA SECRETARIA QUE
            // POR UM MOTIVO OU OUTRO NAO SE ENCONTRAM NA BASE DE DADOS

            if (degreeType.equals(TipoCurso.MESTRADO_OBJ) && (number.intValue() < 5411))
            {
                number = new Integer(5411);
            }

            return new Integer(number.intValue() + 1);

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

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return readSpan(Student.class, criteria, numberOfElementsInSpan, spanNumber);
    }

    public Integer countAll()
    {
        return new Integer(count(Student.class, new Criteria()));
    }

}