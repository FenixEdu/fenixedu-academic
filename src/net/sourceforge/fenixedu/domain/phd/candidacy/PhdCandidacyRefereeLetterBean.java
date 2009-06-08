package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class PhdCandidacyRefereeLetterBean implements Serializable {

    static private final long serialVersionUID = 6105525451822275989L;

    private ApplicantOverallPromise overallPromise;
    private String comparisonGroup;
    private ApplicantRank rank;
    private String rankValue;
    private String refereeName;
    private String refereePosition;
    private String refereeInstituition;
    private String refereeAddress;
    private String refereeCity;
    private String refereeZipCode;
    private DomainReference<Country> refereeCountry;
    private String refereePhone;
    private LocalDate date;
    private String comments;

    private transient InputStream file;
    private byte[] fileContent;
    private String filename;

    public PhdCandidacyRefereeLetterBean() {
    }

    public ApplicantOverallPromise getOverallPromise() {
	return overallPromise;
    }

    public void setOverallPromise(ApplicantOverallPromise overallPromise) {
	this.overallPromise = overallPromise;
    }

    public String getComparisonGroup() {
	return comparisonGroup;
    }

    public void setComparisonGroup(String comparisonGroup) {
	this.comparisonGroup = comparisonGroup;
    }

    public ApplicantRank getRank() {
	return rank;
    }

    public void setRank(ApplicantRank rank) {
	this.rank = rank;
    }

    public String getRankValue() {
	return rankValue;
    }

    public void setRankValue(String rankValue) {
	this.rankValue = rankValue;
    }

    public String getRefereeName() {
	return refereeName;
    }

    public void setRefereeName(String refereeName) {
	this.refereeName = refereeName;
    }

    public String getRefereePosition() {
	return refereePosition;
    }

    public void setRefereePosition(String refereePosition) {
	this.refereePosition = refereePosition;
    }

    public String getRefereeInstituition() {
	return refereeInstituition;
    }

    public void setRefereeInstituition(String refereeInstituition) {
	this.refereeInstituition = refereeInstituition;
    }

    public String getRefereeAddress() {
	return refereeAddress;
    }

    public void setRefereeAddress(String refereeAddress) {
	this.refereeAddress = refereeAddress;
    }

    public String getRefereeCity() {
	return refereeCity;
    }

    public void setRefereeCity(String refereeCity) {
	this.refereeCity = refereeCity;
    }

    public String getRefereeZipCode() {
	return refereeZipCode;
    }

    public void setRefereeZipCode(String refereeZipCode) {
	this.refereeZipCode = refereeZipCode;
    }

    public Country getRefereeCountry() {
	return (this.refereeCountry != null) ? this.refereeCountry.getObject() : null;
    }

    public void setRefereeCountry(Country refereeCountry) {
	this.refereeCountry = (refereeCountry != null) ? new DomainReference<Country>(refereeCountry) : null;
    }

    public String getRefereePhone() {
	return refereePhone;
    }

    public void setRefereePhone(String refereePhone) {
	this.refereePhone = refereePhone;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public InputStream getFile() {
	return file;
    }

    public void setFile(InputStream file) {
	this.file = file;

	if (file != null) {
	    final ByteArrayOutputStream result = new ByteArrayOutputStream();
	    try {
		FileUtils.copy(this.file, result);
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }

	    this.fileContent = result.toByteArray();
	} else {
	    this.fileContent = null;
	}
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public byte[] getFileContent() {
	return this.fileContent;
    }

    public boolean hasFileContent() {
	return this.fileContent != null;
    }
}
