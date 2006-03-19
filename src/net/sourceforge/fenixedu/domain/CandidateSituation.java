/*
 * CandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 15:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CandidateSituation extends CandidateSituation_Base {

	public CandidateSituation() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public CandidateSituation(Date date, String remarks, State validation,
			MasterDegreeCandidate masterDegreeCandidate,
			SituationName situation) {
		this();
		setMasterDegreeCandidate(masterDegreeCandidate);
		setSituation(situation);
		setDate(date);
		setRemarks(remarks);
		setValidation(validation);
	}

}
