/*
 * StudentOJB.java
 * 
 * Created on 28 December 2002, 17:19
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Ricardo Nortadas
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.ojb.broker.query.Criteria;

public class StudentOJB extends PersistentObjectOJB implements IPersistentStudent {

    /**
     * @deprecated
     */
    public IStudent readByNumeroAndEstado(Integer numero, Integer estado, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("number", numero);
        crit.addEqualTo("state", estado);
        crit.addEqualTo("degreeType", degreeType.getTipoCurso());
        return (IStudent) queryObject(Student.class, crit);

    }

    public IStudent readByUsername(String username) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("person.username", username);

        IStudent student = (IStudent) queryObject(Student.class, crit);

        return student;

    }

    //	---------------------------------------------------------------------------------------------------------

    // feitos por David \ Ricardo

    public void delete(IStudent student) throws ExcepcaoPersistencia {
        try {
            super.delete(student);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public IStudent readStudentByDegreeTypeAndPerson(TipoCurso degreeType, IPerson person)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeType", degreeType);
        crit.addEqualTo("person.username", person.getUsername());
        crit.addEqualTo("person.numeroDocumentoIdentificacao", person.getNumeroDocumentoIdentificacao());
        crit.addEqualTo("person.idDocumentType", person.getIdDocumentType());
        return (IStudent) queryObject(Student.class, crit);

    }

    public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("number", number);
        criteria.addEqualTo("degreeType", degreeType);

        return (IStudent) queryObject(Student.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Student.class, new Criteria());
    }

    public IStudent readByPersonAndDegreeType(IPerson person, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("person.idInternal", person.getIdInternal());
        crit.addEqualTo("degreeType", degreeType);
        return (IStudent) queryObject(Student.class, crit);

    }

    public Integer generateStudentNumber(TipoCurso degreeType) throws ExcepcaoPersistencia {

        Integer number = new Integer(0);
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeType", degreeType);
        List result = queryList(Student.class, crit, "number", false);

        if ((result != null) && (result.size() != 0)) {
            number = ((IStudent) result.get(0)).getNumber();
        }

        // FIXME: ISTO E UMA SOLUCAO TEMPORARIA DEVIDO A EXISTIREM ALUNOS
        // NA SECRETARIA QUE
        // POR UM MOTIVO OU OUTRO NAO SE ENCONTRAM NA BASE DE DADOS

        if (degreeType.equals(TipoCurso.MESTRADO_OBJ) && (number.intValue() < 5411)) {
            number = new Integer(5411);
        }

        return new Integer(number.intValue() + 1);

    }

    public List readbyPerson(IPerson person) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("personKey", person.getIdInternal());

        return queryList(Student.class, criteria);
    }

    public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(String studentName,
            String idNumber, IDDocumentType idType, Integer studentNumber)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        if (studentName != null) {
            criteria.addLike("person.nome", studentName);
        }

        if (idNumber != null) {
            criteria.addEqualTo("person.numeroDocumentoIdentificacao", idNumber);
        }

        if (idType != null) {
            criteria.addEqualTo("person.idDocumentType", idType);
        }

        if (studentNumber != null) {
            criteria.addEqualTo("number", studentNumber);
        }

        criteria.addEqualTo("degreeType", TipoCurso.MESTRADO_OBJ.getTipoCurso());

        return queryList(Student.class, criteria);
    }

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return readSpan(Student.class, criteria, numberOfElementsInSpan, spanNumber);
    }

    public Integer countAll() {
        return new Integer(count(Student.class, new Criteria()));
    }

    public List readStudentbyRegistrationYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyRegistrationYear", executionYear.getIdInternal());
        return queryList(Student.class, criteria);
    }

    public List readStudentByPersonRole(IRole role) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.personRoles.roleType", role.getRoleType());
        return queryList(Student.class, criteria);
    }

    public List readAllBetweenNumbers(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addGreaterOrEqualThan("number", fromNumber);
        criteria.addLessOrEqualThan("number", toNumber);
        return queryList(Student.class, criteria);
    }

    public List readAllWithPayedTuition() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("payedTuition", new Boolean(true));
        return queryList(Student.class, criteria);
    }

}