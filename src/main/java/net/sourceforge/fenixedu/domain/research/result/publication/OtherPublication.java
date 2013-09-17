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
 * (Misc, Booklet, Unpublished, ...) Use this type when nothing else fits.
 * Required fields: title. Optional fields: otherPublicationType, howpublished,
 * month, year, note, numberPages, language, publisher, address
 */

public class OtherPublication extends OtherPublication_Base {

    private static final String usedSchema = "result.publication.presentation.OtherPublication";

    public OtherPublication() {
        super();
    }

    public OtherPublication(Person participator, String title, MultiLanguageString keywords, String publisher, Integer year,
            String howPublished, MultiLanguageString note, String address, String otherPublicationType, Integer numberPages,
            String language, Country country, Month month, String url) {
        this();
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title);
        super.setCreatorParticipation(participator, ResultParticipationRole.Author);
        fillAllAttributes(title, keywords, publisher, year, howPublished, note, address, otherPublicationType, numberPages,
                language, country, month, url);
    }

    public void setEditAll(String title, MultiLanguageString keywords, String publisher, Integer year, String howPublished,
            MultiLanguageString note, String address, String otherPublicationType, Integer numberPages, String language,
            Country country, Month month, String url) {
        check(this, ResultPredicates.writePredicate);
        super.checkRequiredParameters(keywords, note);
        checkRequiredParameters(title);
        fillAllAttributes(title, keywords, publisher, year, howPublished, note, address, otherPublicationType, numberPages,
                language, country, month, url);
        super.setModifiedByAndDate();
    }

    private void fillAllAttributes(String title, MultiLanguageString keywords, String publisher, Integer year,
            String howPublished, MultiLanguageString note, String address, String otherPublicationType, Integer numberPages,
            String language, Country country, Month month, String url) {
        super.setTitle(title);
        super.setPublisher(publisher);
        super.setYear(year);
        super.setHowPublished(howPublished);
        super.setNote(note);
        super.setAddress(address);
        super.setOtherPublicationType(otherPublicationType);
        super.setNumberPages(numberPages);
        super.setLanguage(language);
        super.setCountry(country);
        super.setMonth(month);
        super.setUrl(url);
        super.setKeywords(keywords);
    }

    @Override
    public String getResume() {
        String resume = getParticipationsAndTitleString();
        if ((getOtherPublicationType() != null) && (getOtherPublicationType().length() > 0)) {
            resume = resume + getOtherPublicationType() + ", ";
        }
        if ((getYear() != null) && (getYear() > 0)) {
            resume = resume + getYear() + ", ";
        }
        if (getPublisher() != null) {
            resume = resume + getPublisher() + ", ";
        }

        resume = finishResume(resume);
        return resume;
    }

    @Override
    public BibtexEntry exportToBibtexEntry() {
        BibtexFile bibtexFile = new BibtexFile();
        BibtexEntry bibEntry = bibtexFile.makeEntry("otherpublication", generateBibtexKey());
        if ((getTitle() != null) && (getTitle().length() > 0)) {
            bibEntry.setField("title", bibtexFile.makeString(getTitle()));
        }
        if ((getYear() != null) && (getYear() > 0)) {
            bibEntry.setField("year", bibtexFile.makeString(getYear().toString()));
        }
        if (getMonth() != null) {
            bibEntry.setField("month", bibtexFile.makeString(getMonth().toString().toLowerCase()));
        }
        if ((getOtherPublicationType() != null) && (getOtherPublicationType().length() > 0)) {
            bibEntry.setField("type", bibtexFile.makeString(getOtherPublicationType()));
        }
        if ((getHowPublished() != null) && (getHowPublished().length() > 0)) {
            bibEntry.setField("howpublished", bibtexFile.makeString(getHowPublished()));
        }
        if (getPublisher() != null) {
            bibEntry.setField("publisher", bibtexFile.makeString(getPublisher()));
        }
        if ((getAddress() != null) && (getAddress().length() > 0)) {
            bibEntry.setField("address", bibtexFile.makeString(getAddress()));
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

    private void checkRequiredParameters(String title) {
        if ((title == null) || (title.length() == 0)) {
            throw new DomainException("error.researcher.OtherPublication.title.null");
        }
    }

    @Override
    public void setTitle(String title) {
        throw new DomainException("error.researcher.OtherPublication.call", "setTitle");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.researcher.OtherPublication.call", "setYear");
    }

    @Override
    public void setHowPublished(String howPublished) {
        throw new DomainException("error.researcher.OtherPublication.call", "setHowPublished");
    }

    @Override
    public void setAddress(String address) {
        throw new DomainException("error.researcher.OtherPublication.call", "setAddress");
    }

    @Override
    public void setOtherPublicationType(String otherPublicationType) {
        throw new DomainException("error.researcher.OtherPublication.call", "setOtherPublicationType");
    }

    @Override
    public void setNote(MultiLanguageString note) {
        throw new DomainException("error.researcher.OtherPublication.call", "setNote");
    }

    @Override
    public void setLanguage(String language) {
        throw new DomainException("error.researcher.OtherPublication.call", "setLanguage");
    }

    @Override
    public void setMonth(Month month) {
        throw new DomainException("error.researcher.OtherPublication.call", "setMonth");
    }

    @Override
    public void setUrl(String url) {
        throw new DomainException("error.researcher.OtherPublication.call", "setUrl");
    }

    @Override
    public void setNumberPages(Integer numberPages) {
        throw new DomainException("error.researcher.OtherPublication.call", "setNumberPages");
    }

    @Override
    public void setOrganization(String organization) {
        throw new DomainException("error.researcher.OtherPublication.call", "setOrganization");
    }

    @Override
    public void setPublisher(String publisher) {
        throw new DomainException("error.researcher.OtherPublication.call", "setPublisher");
    }

    @Override
    public String getSchema() {
        return usedSchema;
    }
    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasHowPublished() {
        return getHowPublished() != null;
    }

    @Deprecated
    public boolean hasOtherPublicationType() {
        return getOtherPublicationType() != null;
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
