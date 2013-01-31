/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeThesisState;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListMasterDegreeProofsBean implements Serializable {

	private Degree degree;

	private Integer year;

	private MasterDegreeThesisState thesisState;

	private Boolean generateFile;

	public ListMasterDegreeProofsBean() {
		year = new YearMonthDay().getYear();
	}

	public List<MasterDegreeProofVersion> getMasterDegreeProofs() {
		if (this.degree != null) {
			return MasterDegreeProofVersion.getConcludedForDegreeAndSinceYear(getDegree(), year);
		}
		return null;
	}

	public Degree getDegree() {
		return this.degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Boolean getGenerateFile() {
		return generateFile != null && generateFile;
	}

	public void setGenerateFile(Boolean generateFile) {
		this.generateFile = generateFile;
	}

	public MasterDegreeThesisState getThesisState() {
		return thesisState;
	}

	public void setThesisState(MasterDegreeThesisState thesisState) {
		this.thesisState = thesisState;
	}

}
