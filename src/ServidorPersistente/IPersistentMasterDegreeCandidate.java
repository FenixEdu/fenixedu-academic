/*
 * IPersistentMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 11:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
package ServidorPersistente;

import Dominio.IMasterDegreeCandidate;

public interface IPersistentMasterDegreeCandidate {
    public IMasterDegreeCandidate readMasterDegreeCandidateByUsername(String username) throws ExcepcaoPersistencia;
    public IMasterDegreeCandidate readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(Integer candidateNumber, String applicationYear, String degreeCode) throws ExcepcaoPersistencia;
    public void writeMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;
    public void delete(IMasterDegreeCandidate masterDegreeCandidate) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;

} // End of class definition
