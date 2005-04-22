/*
 * MasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 9:53
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.util.Specialization;
import net.sourceforge.fenixedu.util.State;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

	private Specialization specialization = null;

	// Instance from class Country

	private IPerson person;

	// List of Situations
	private List situations;

	public MasterDegreeCandidate() {
		setMajorDegree(null);
		setExecutionDegree(null);
		setCandidateNumber(null);
		specialization = null;
		setMajorDegreeSchool(null);
		setMajorDegreeYear(null);
		setAverage(null);
		situations = null;
		person = null;
	}

	public MasterDegreeCandidate(IPerson person,
			IExecutionDegree executionDegree, Integer candidateNumber,
			Specialization specialization, String majorDegree,
			String majorDegreeSchool, Integer majorDegreeYear, Double average) {
		this.person = person;
		this.setExecutionDegree(executionDegree);
		setCandidateNumber(candidateNumber);
		this.specialization = specialization;
		setMajorDegree(majorDegree);
		setMajorDegreeSchool(majorDegreeSchool);
		setMajorDegreeYear(majorDegreeYear);
		setAverage(average);

	}

	public boolean equals(Object o) {

		boolean result = false;
		if (o instanceof IMasterDegreeCandidate) {

			result = getIdInternal().equals(
					((IMasterDegreeCandidate) o).getIdInternal());
			// result =
			// ((this.person.equals(((MasterDegreeCandidate)o).getPerson())) &&
			// (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization()))
			// &&
			// (this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree)))
			// ||
			//				 
			// ((this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree))
			// &&
			// (this.candidateNumber.equals(((MasterDegreeCandidate)o).getCandidateNumber()))
			// &&
			// (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization())));
		}
		return result;
	}

	public String toString() {
		String result = "Master Degree Candidate :\n";
		result += "\n  - Internal Code : " + getIdInternal();
		result += "\n  - Person : " + person;
		result += "\n  - Major Degree : " + getMajorDegree();
		result += "\n  - Candidate Number : " + getCandidateNumber();
		result += "\n  - Specialization : " + specialization;
		result += "\n  - Major Degree School : " + getMajorDegreeSchool();
		result += "\n  - Major Degree Year : " + getMajorDegreeYear();
		result += "\n  - Major Degree Average : " + getAverage();
		result += "\n  - Master Degree : " + getExecutionDegree();
		result += "\n  - Specialization Area : " + getSpecializationArea();
		result += "\n  - Substitute Order : " + getSubstituteOrder();
		result += "\n  - Given Credits : " + getGivenCredits();
		result += "\n  - Given Credits Remarks : " + getGivenCreditsRemarks();

		return result;
	}

	public ICandidateSituation getActiveCandidateSituation() {
		Iterator iterator = this.getSituations().iterator();
		while (iterator.hasNext()) {
			ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator
					.next();
			if (candidateSituationTemp.getValidation().equals(
					new State(State.ACTIVE))) {
				return candidateSituationTemp;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public IPerson getPerson() {
		return person;
	}

	/**
	 * @return
	 */
	public List getSituations() {
		return situations;
	}

	/**
	 * @return
	 */
	public Specialization getSpecialization() {
		return specialization;
	}

	/**
	 * @param pessoa
	 */
	public void setPerson(IPerson pessoa) {
		person = pessoa;
	}

	/**
	 * @param set
	 */
	public void setSituations(List list) {
		situations = list;
	}

	/**
	 * @param specialization
	 */
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

} // End of class definition
