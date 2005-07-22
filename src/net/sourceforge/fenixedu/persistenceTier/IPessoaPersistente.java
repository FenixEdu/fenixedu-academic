package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public interface IPessoaPersistente extends IPersistentObject {

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;

    public List<IPerson> findPersonByName(String name) throws ExcepcaoPersistencia;

    public List<IPerson> readActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia;
    
    public Integer countActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer spanNumber)
            throws ExcepcaoPersistencia;

    public List<IPerson> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia;
    

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia;

    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;
    
    public List<IPerson> readPersonsBySubName(String subName) throws ExcepcaoPersistencia;

}