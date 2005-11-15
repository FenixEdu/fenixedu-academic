package net.sourceforge.fenixedu.persistenceTier;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public interface IPessoaPersistente extends IPersistentObject {

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia;

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia;

    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;

    public List<IPerson> readPersonsBySubName(String subName) throws ExcepcaoPersistencia;
    
    public List<IPerson> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
    
    public Collection<IPerson> readByIdentificationDocumentNumber(String identificationDocumentNumber)  throws ExcepcaoPersistencia;
    
    public boolean emailOwnedByFenixPerson(Collection<String> emails) throws ExcepcaoPersistencia;;
}