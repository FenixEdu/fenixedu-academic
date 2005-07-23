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

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePersonalStudentInfo implements IService {

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoPersistencia, ExcepcaoInexistente  {

        ISuportePersistente sp = null;
        IPerson person = null;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                newInfoPerson.getIdInternal());

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        // Get new Country
        ICountry country = null;
        if ((newInfoPerson.getInfoPais() != null)
            && (newInfoPerson.getInfoPais().getNationality().length() != 0)) {
            if ((person.getPais() == null)
                    || (!newInfoPerson.getInfoPais().getNationality().equals(
                            person.getPais().getNationality()))) {
                country = sp.getIPersistentCountry().readCountryByNationality(
                        newInfoPerson.getInfoPais().getNationality());
            }
        }

        // Change personal Information
        person.edit(newInfoPerson, country);
        
        InfoPerson infoPerson = InfoPersonWithInfoCountry.newInfoFromDomain(person);

        return infoPerson;
    }
}