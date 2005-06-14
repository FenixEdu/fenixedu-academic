/*
 * IPessoaPersistente.java
 * 
 * Created on 15 de Outubro de 2002, 15:03
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public interface IPessoaPersistente extends IPersistentObject {
    /**
     * 
     * @param pessoa
     * @throws ExcepcaoPersistencia
     * @throws ExistingPersistentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @deprecated
     */

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;

    public List findPersonByName(String name) throws ExcepcaoPersistencia;

    public List findActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia;

    public List findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia;

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia;

    //public List findPersonByNameAndEmailAndUsernameAndDocumentId(String name,
    // String email, String username, String documentIdNumber) throws
    // ExcepcaoPersistencia;
    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;

}