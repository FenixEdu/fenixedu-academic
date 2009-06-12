package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class PhdCandidacyRefereeLetterBean implements Serializable {

    static private final long serialVersionUID = 6105525451822275989L;

    private DomainReference<PhdCandidacyReferee> candidacyReferee;
    private DomainReference<PhdCandidacyRefereeLetter> letter;

    private ApplicantOverallPromise overallPromise;
    private String comparisonGroup;
    private String rankValue;
    private ApplicantRank rank;

    private String refereeName;
    private String refereePosition;
    private String refereeInstitution;
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

    private String existingFileFilename;
    private String existingFileSize;

    public PhdCandidacyRefereeLetterBean() {
	setDate(new LocalDate());
    }

    public PhdCandidacyRefereeLetterBean(final PhdCandidacyRefereeLetter letter) {
	setOverallPromise(letter.getOverallPromise());
	setComparisonGroup(letter.getComparisonGroup());
	setRank(letter.getRank());
	setRankValue(letter.getRankValue());
	setRefereeName(letter.getRefereeName());
	setRefereePosition(letter.getRefereePosition());
	setRefereeInstitution(letter.getRefereeInstitution());
	setRefereeAddress(letter.getRefereeAddress());
	setRefereeCity(letter.getRefereeCity());
	setRefereeZipCode(letter.getRefereeZipCode());
	setRefereeCountry(letter.getRefereeCountry());
	setRefereePhone(letter.getRefereePhone());
	setDate(letter.getDate());
	setComments(letter.getComments());

	if (letter.hasFile()) {
	    // TODO: check this
	    setExistingFileFilename(letter.getFile().getFilename());
	    setExistingFileSize(new BigDecimal(letter.getFile().getSize()).divide(new BigDecimal("1024")).setScale(2,
		    RoundingMode.HALF_EVEN).toString());
	}

	setCandidacyReferee(letter.getCandidacyReferee());
	setLetter(letter);
    }

    public PhdCandidacyReferee getCandidacyReferee() {
	return (this.candidacyReferee != null) ? this.candidacyReferee.getObject() : null;
    }

    public void setCandidacyReferee(final PhdCandidacyReferee candidacyReferee) {
	this.candidacyReferee = (candidacyReferee != null) ? new DomainReference<PhdCandidacyReferee>(candidacyReferee) : null;
    }

    public PhdCandidacyRefereeLetter getLetter() {
	return (this.letter != null) ? this.letter.getObject() : null;
    }

    public void setLetter(final PhdCandidacyRefereeLetter letter) {
	this.letter = (letter != null) ? new DomainReference<PhdCandidacyRefereeLetter>(letter) : null;
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

    public String getRefereeInstitution() {
	return refereeInstitution;
    }

    public void setRefereeInstitution(String refereeInstitution) {
	this.refereeInstitution = refereeInstitution;
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

    public Person getPerson() {
	return getCandidacyReferee().getPhdProgramCandidacyProcess().getPerson();
    }

    public PhdProgramFocusArea getFocusArea() {
	return getCandidacyReferee().getPhdProgramCandidacyProcess().getIndividualProgramProcess().getPhdProgramFocusArea();
    }

    public String getEmail() {
	return getCandidacyReferee().getEmail();
    }

    public boolean hasLetter() {
	return getLetter() != null;
    }

    public void removeFile() {
	setFile(null);
    }

    public String getExistingFileFilename() {
	return existingFileFilename;
    }

    public void setExistingFileFilename(String existingFileFilename) {
	this.existingFileFilename = existingFileFilename;
    }

    public String getExistingFileSize() {
	return existingFileSize;
    }

    public void setExistingFileSize(String existingFileSize) {
	this.existingFileSize = existingFileSize;
    }

}
