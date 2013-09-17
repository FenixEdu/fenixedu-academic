package net.sourceforge.fenixedu.domain.research.result.publication;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.Month;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;

/**
 * A report published by a school or other institution, usually numbered within
 * a series. Required fields: author, title, institution, year. Optional fields:
 * type, number, address, month, note.
 * 
 * Extra from previous publications: numberPages, language
 */
public class TechnicalReport extends TechnicalReport_Base {

    private static final String usedSchema = "result.publication.presentation.TechnicalReport";

    public TechnicalReport() {
        super();
    }

    public TechnicalReport(Person participator, String title, MultiLanguageString keywords, String institution, Integer year,
            String technicalReportType, String number, String address, MultiLanguageString note, Integer numberPages,
            String language, Month month, String url) {
        this();
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title, institution, year);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, keywords, institution, year, technicalReportType, number, address, note, numberPages, language,
                month, url);
    }

    public void setEditAll(String title, MultiLanguageString keywords, String institution, Integer year,
            String technicalReportType, String number, String address, MultiLanguageString note, Integer numberPages,
            String language, Month month, String url) {
        check(this, ResultPredicates.writePredicate);
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title, institution, year);
        fillAllAttributes(title, keywords, institution, year, technicalReportType, number, address, note, numberPages, language,
                month, url);
        super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, String institution, Integer year,
            String technicalReportType, String number, String address, MultiLanguageString note, Integer numberPages,
            String language, Month month, String url) {
        super.setTitle(title);
        super.setOrganization(institution);
        super.setYear(year);
        super.setTechnicalReportType(technicalReportType);
        super.setNumber(number);
        super.setAddress(address);
        super.setNote(note);
        super.setNumberPages(numberPages);
        super.setLanguage(language);
        super.setMonth(month);
        super.setUrl(url);
        super.setKeywords(keywords);
    }

    private void checkRequiredParameters(String title, String institution, Integer year) {
        if ((title == null) || (title.length() == 0)) {
            throw new DomainException("error.researcher.TechnicalReport.title.null");
        }
        if (institution == null) {
            throw new DomainException("error.researcher.TechnicalReport.institution.null");
        }
        if (year == null) {
            throw new DomainException("error.researcher.TechnicalReport.year.null");
        }
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if (getOrganization() != null) {
            resume = resume + getOrganization() + ", ";
        }
        if ((getYear() != null) && (getYear() > 0)) {
            resume = resume + getYear() + ", ";
        }
        if ((getNumber() != null) && (getNumber().length() > 0)) {
            resume = resume + "No. " + getNumber() + ", ";
        }

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();

        BibtexEntry bibEntry = bibtexFile.makeEntry("techreport", generateBibtexKey());
        bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        if (getOrganization() != null) {
            bibEntry.setField("organization", bibtexFile.makeString(getOrganization()));
        }
        bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        if ((getTechnicalReportType() != null) && (getTechnicalReportType().length() > 0)) {
            bibEntry.setField("type", bibtexFile.makeString(getTechnicalReportType()));
        }
        if ((getNumber() != null) && (getNumber().length() > 0)) {
            bibEntry.setField("number", bibtexFile.makeString(getNumber()));
        }
        if ((getAddress() != null) && (getAddress().length() > 0)) {
            bibEntry.setField("address", bibtexFile.makeString(getAddress()));
        }
        if (getMonth() != null) {
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        }
        if ((getNote() != null) && (getNote().hasContent())) {
            bibEntry.setField("note", bibtexFile.makeString(getNote().getContent()));
        }

        BibtexPersonList authorsList = getBibtexAuthorsList(bibtexFile, getAuthors());
        if (authorsList != null) {
            BibtexString bplString = bibtexFile.makeString(bibtexPersonListToString(authorsList));
            bibEntry.setField("author", bplString);
        }

        return bibEntry;
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setYear");
    }

    @Override
    public void setTechnicalReportType(String technicalReportType) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setTechnicalReportType");
    }

    @Override
    public void setNumber(String number) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setNumber");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setAddress");
    }

    @Override
    public void setNote(MultiLanguageString note) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setNote");
    }

    @Override
    public void setNumberPages(Integer numberPages) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setNumberPages");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setUrl");
    }

    @Override
    public void setCountry(Country country) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setCountry");
    }

    @Override
    public void setOrganization(String organization) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setOrganization");
    }

    @Override
    public void setPublisher(String publisher) {
        throw new DomainException("error.researcher.TechnicalReport.call", "setPublisher");
    }

    @Override
    public String getSchema() {
        return usedSchema;
    }
    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasTechnicalReportType() {
        return getTechnicalReportType() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasLanguage() {
        return getLanguage() != null;
    }

    @Deprecated
    public boolean hasNumberPages() {
        return getNumberPages() != null;
    }

}
