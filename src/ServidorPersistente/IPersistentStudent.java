package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public interface IPersistentStudent extends IPersistentObject {

    // feitos por David \ Ricardo
    public IStudent readStudentByNumberAndDegreeType(Integer number, TipoCurso degreeType)
            throws ExcepcaoPersistencia;

    public IStudent readStudentByDegreeTypeAndPerson(TipoCurso degreeType, IPessoa person)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IStudent student) throws ExcepcaoPersistencia;

    // feitos pelo Nortadas
    public IStudent readByUsername(String username) throws ExcepcaoPersistencia;

    /**
     * @param IPessoa
     * @param TipoCurso
     * @return IStudent
     */
    public IStudent readByPersonAndDegreeType(IPessoa person, TipoCurso degreeType)
            throws ExcepcaoPersistencia;

    /**
     * @param curso
     * @return
     */
    public Integer generateStudentNumber(TipoCurso curso) throws ExcepcaoPersistencia;

    /**
     * @param Person
     * @return All the students associated to this Person
     */
    public List readbyPerson(IPessoa pessoa) throws ExcepcaoPersistencia;

    public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(String studentName,
            String idNumber, TipoDocumentoIdentificacao idType, Integer studentNumber)
            throws ExcepcaoPersistencia;

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia;

    public Integer countAll() throws ExcepcaoPersistencia;

    public List readStudentbyRegistrationYear(IExecutionYear executionYear) throws ExcepcaoPersistencia;

    public List readStudentByPersonRole(IRole role) throws ExcepcaoPersistencia;

    public List readAllBetweenNumbers(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia;

    public List readAllWithPayedTuition() throws ExcepcaoPersistencia;
}