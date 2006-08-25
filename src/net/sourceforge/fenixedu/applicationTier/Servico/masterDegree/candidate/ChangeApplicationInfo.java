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

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePersonalContactInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.joda.time.YearMonthDay;

public class ChangeApplicationInfo extends Service {

    public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate,
	    InfoPersonEditor infoPersonEditor, IUserView userView, Boolean isNewPerson)
	    throws FenixServiceException {

	final ExecutionDegree executionDegree = rootDomainObject
		.readExecutionDegreeByOID(newMasterDegreeCandidate.getInfoExecutionDegree()
			.getIdInternal());

	Person person = Person.readByDocumentIdNumberAndIdDocumentType(newMasterDegreeCandidate
		.getInfoPerson().getNumeroDocumentoIdentificacao(), newMasterDegreeCandidate
		.getInfoPerson().getTipoDocumentoIdentificacao());

	MasterDegreeCandidate existingMasterDegreeCandidate = person
		.getMasterDegreeCandidateByExecutionDegree(executionDegree);
	if (existingMasterDegreeCandidate == null) {
	    throw new ExcepcaoInexistente("error.changeApplicationInfo.noCandidate");
	}

	if (isNewPerson) {
	    Country country = null;
	    if ((infoPersonEditor.getInfoPais() != null)) {
		country = Country.readCountryByNationality(infoPersonEditor.getInfoPais()
			.getNationality());
	    }

	    person = existingMasterDegreeCandidate.getPerson();
	    person.edit(infoPersonEditor, country);

	} else {
	    new ChangePersonalContactInformation().run(userView, infoPersonEditor);
	}

	// Change Candidate Information
	existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
	existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
	existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate
		.getMajorDegreeSchool());
	existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
	existingMasterDegreeCandidate.setSpecializationArea(newMasterDegreeCandidate
		.getSpecializationArea());

	CandidateSituation oldCandidateSituation = existingMasterDegreeCandidate
		.getActiveCandidateSituation();
	oldCandidateSituation.setValidation(new State(State.INACTIVE));

	CandidateSituation activeCandidateSituation = new CandidateSituation();
	activeCandidateSituation.setDateYearMonthDay(new YearMonthDay());
	activeCandidateSituation.setSituation(SituationName.PENDENT_COM_DADOS_OBJ);
	activeCandidateSituation.setValidation(new State(State.ACTIVE));
	activeCandidateSituation.setMasterDegreeCandidate(existingMasterDegreeCandidate);
	existingMasterDegreeCandidate.getSituations().add(activeCandidateSituation);

	return InfoMasterDegreeCandidateWithInfoPerson.newInfoFromDomain(existingMasterDegreeCandidate);

    }

}