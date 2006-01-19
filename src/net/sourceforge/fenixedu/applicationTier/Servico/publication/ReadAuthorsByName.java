package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonsByName;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadAuthorsByName extends Service {

    public List<InfoAuthor> run(String stringtoSearch) throws ExcepcaoPersistencia {

        List<InfoPerson> infoPersons = new ReadPersonsByName().run(stringtoSearch);
        
        List<InfoAuthor> infoAuthors = new ArrayList<InfoAuthor>(infoPersons.size());
        
        for (InfoPerson infoPerson : infoPersons) {
            InfoAuthor infoAuthor = new InfoAuthor();
            infoAuthor.setAuthor(infoPerson.getNome());
            infoAuthor.setIdInternal(infoPerson.getIdInternal());
            infoAuthor.setInfoPessoa(infoPerson);
            infoAuthor.setKeyPerson(infoPerson.getIdInternal());
            if (infoPerson.getInfoExternalPerson() != null) {
                infoAuthor.setOrganization(infoPerson.getInfoExternalPerson().getInfoInstitution().getName());
            }
            
            infoAuthors.add(infoAuthor);
        }
        
        return infoAuthors;
        
    }
}