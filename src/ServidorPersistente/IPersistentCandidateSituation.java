/*
 * IPersistentCandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 16:07
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
package ServidorPersistente;

import Dominio.ICandidateSituation;

public interface IPersistentCandidateSituation {
    public ICandidateSituation readCandidateSituation(Integer candidateNumber, String degreeCode, Integer applicationYear) throws ExcepcaoPersistencia;
    public void writeCandidateSituation(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia;
    public void delete(ICandidateSituation candidateSituation) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;

} // End of class definition



