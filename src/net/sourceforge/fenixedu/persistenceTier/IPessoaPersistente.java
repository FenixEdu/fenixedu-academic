package net.sourceforge.fenixedu.persistenceTier;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public interface IPessoaPersistente extends IPersistentObject {

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia;

    public Person lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia;

    public List<Person> readPersonsBySubName(String subName) throws ExcepcaoPersistencia;
    
    public List<Person> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia;
    
    public Collection<Person> readByIdentificationDocumentNumber(String identificationDocumentNumber)  throws ExcepcaoPersistencia;
    
    public boolean emailOwnedByFenixPerson(Collection<String> emails) throws ExcepcaoPersistencia;;
}