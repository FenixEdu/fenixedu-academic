/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a>
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 */
public class CreateMasterDegreeCandidate {

    @Atomic
    public static InfoMasterDegreeCandidate run(Specialization degreeType, String executionDegreeID, String name,
            String identificationDocumentNumber, IDDocumentType identificationDocumentType) throws Exception {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        // Read the Execution of this degree in the current execution Year
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber, identificationDocumentType);

        if (person == null) {
            // Create the new Person
            person = new Person(name, identificationDocumentNumber, identificationDocumentType, Gender.MALE);
        } else {
            MasterDegreeCandidate existingMasterDegreeCandidate =
                    person.getMasterDegreeCandidateByExecutionDegree(executionDegree);
            if (existingMasterDegreeCandidate != null) {
                throw new ExistingServiceException();
            }
        }

        person.addPersonRoleByRoleType(RoleType.PERSON);

        // Create the Candidate
        MasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();

        // Set the Candidate's Situation
        new CandidateSituation(Calendar.getInstance().getTime(), "Pré-Candidatura. Pagamento da candidatura por efectuar.",
                new State(State.ACTIVE), masterDegreeCandidate, new SituationName(SituationName.PRE_CANDIDATO));

        masterDegreeCandidate.setSpecialization(degreeType);
        masterDegreeCandidate.setExecutionDegree(executionDegree);
        masterDegreeCandidate.setCandidateNumber(executionDegree.generateCandidateNumberForSpecialization(degreeType));

        masterDegreeCandidate.setPerson(person);

        // Return the new Candidate
        InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(masterDegreeCandidate);
        InfoExecutionDegree infoExecutionDegree =
                InfoExecutionDegree.newInfoFromDomain(masterDegreeCandidate.getExecutionDegree());
        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);
        return infoMasterDegreeCandidate;
    }
}