package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public interface IPersistentStudent extends IPersistentObject {

    // feitos por David \ Ricardo
    public IStudent readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    // feitos pelo Nortadas
    public IStudent readByUsername(String username) throws ExcepcaoPersistencia;

    /**
     * @param IPerson
     * @param DegreeType
     * @return IStudent
     */
    public IStudent readByPersonAndDegreeType(Integer personId, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    /**
     * @param curso
     * @return
     */
    public Integer generateStudentNumber(DegreeType curso) throws ExcepcaoPersistencia;

    /**
     * @param Person
     * @return All the students associated to this Person
     */

    public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(String studentName,
            String idNumber, IDDocumentType idType, Integer studentNumber)
            throws ExcepcaoPersistencia;

    public Integer countAll() throws ExcepcaoPersistencia;

    public List readStudentByPersonRole(RoleType roleType) throws ExcepcaoPersistencia;

    public List readAllBetweenNumbers(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia;

    public List readAllWithPayedTuition() throws ExcepcaoPersistencia;
}