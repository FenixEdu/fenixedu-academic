/*
 * ChangeMasterDegreeCandidate.java O Servico ChangeMasterDegreeCandidate altera
 * a informacao de um candidato de Mestrado Nota : E suposto os campos
 * (numeroCandidato, anoCandidatura, chaveCursoMestrado, username) nao se
 * puderem alterar Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangePersonalStudentInfo extends Service {

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoPersistencia, FenixServiceException  {
        
        Person person = (Person) persistentObject.readByOID(Person.class, newInfoPerson.getIdInternal());
        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        // Get new Country
        Country country = null;
        if ((newInfoPerson.getInfoPais() != null)
            && (newInfoPerson.getInfoPais().getNationality().length() != 0)) {
            if ((person.getPais() == null)
                    || (!newInfoPerson.getInfoPais().getNationality().equals(
                            person.getPais().getNationality()))) {
                country = persistentSupport.getIPersistentCountry().readCountryByNationality(
                        newInfoPerson.getInfoPais().getNationality());
            }else{
                country = person.getPais();
            }
        }
        else {
            //If the person country is undefined it is set to default "PORTUGUESA NATURAL DO CONTINENTE" 
            //In a not distance future this will not be needed since the coutry can never be null
            country = (Country) persistentSupport.getIPersistentCountry().readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        }

        // Change personal Information
        person.edit(newInfoPerson, country);
                  
        InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);

        return infoPerson;
    }
}