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
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;

public class ChangePersonalStudentInfo extends Service {

    public InfoPerson run(InfoPersonEditor newInfoPerson) throws FenixServiceException  {
        
        final Person person = (Person) rootDomainObject.readPartyByOID(newInfoPerson.getIdInternal());
        if (person == null) {
            throw new ExcepcaoInexistente("error.changePersonalStudentInfo.noPerson");
        }

        // Get new Country
        Country country = null;
        if ((newInfoPerson.getInfoPais() != null)
            && (newInfoPerson.getInfoPais().getNationality().length() != 0)) {
            if ((person.getNationality() == null)
                    || (!newInfoPerson.getInfoPais().getNationality().equals(
                            person.getNationality().getNationality()))) {
                country = Country.readCountryByNationality(newInfoPerson.getInfoPais().getNationality());
            }else{
                country = person.getNationality();
            }
        }
        else {
            //If the person country is undefined it is set to default "PORTUGUESA NATURAL DO CONTINENTE" 
            //In a not distance future this will not be needed since the coutry can never be null
            country = Country.readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        }

        // Change personal Information
        person.edit(newInfoPerson, country);

        return InfoPerson.newInfoFromDomain(person);
    }
}