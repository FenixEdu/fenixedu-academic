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
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.SituationName;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration {

    @Atomic
    public static List run(String executionDegreeCode) throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        List<SituationName> situationNames =
                Arrays.asList(new SituationName[] { SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ,
                        SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ, SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ,
                        SituationName.ADMITED_CONDICIONAL_OTHER_OBJ, SituationName.ADMITIDO_OBJ,
                        SituationName.ADMITED_SPECIALIZATION_OBJ });

        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeCode);
        List<CandidateSituation> result = executionDegree.getCandidateSituationsInSituation(situationNames);

        if (result.isEmpty()) {
            throw new NonExistingServiceException();
        }

        List candidateList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            CandidateSituation candidateSituation = (CandidateSituation) resultIterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate =
                    InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(candidateSituation.getMasterDegreeCandidate());
            infoMasterDegreeCandidate.setInfoCandidateSituation(InfoCandidateSituation.newInfoFromDomain(candidateSituation));
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;

    }
}