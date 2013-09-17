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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangePersonalStudentInfo {

    @Atomic
    public static InfoPerson run(InfoPersonEditor newInfoPerson) throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        final Person person = (Person) FenixFramework.getDomainObject(newInfoPerson.getExternalId());
        if (person == null) {
            throw new ExcepcaoInexistente("error.changePersonalStudentInfo.noPerson");
        }

        // Get new Country
        Country country = null;
        if ((newInfoPerson.getInfoPais() != null) && (newInfoPerson.getInfoPais().getNationality().length() != 0)) {
            if ((person.getCountry() == null)
                    || (!newInfoPerson.getInfoPais().getNationality().equals(person.getCountry().getNationality()))) {
                country = Country.readCountryByNationality(newInfoPerson.getInfoPais().getNationality());
            } else {
                country = person.getCountry();
            }
        } else {
            country = Country.readDefault();
        }

        // Change personal Information
        person.edit(newInfoPerson, country);

        return InfoPerson.newInfoFromDomain(person);
    }

}