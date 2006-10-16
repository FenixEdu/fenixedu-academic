/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;

import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListMasterDegreeProofsBean implements Serializable {

    private DomainReference<Degree> degree;

    private Integer year;

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
	return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

}
