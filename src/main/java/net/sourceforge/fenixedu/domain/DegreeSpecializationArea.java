package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeSpecializationArea extends DegreeSpecializationArea_Base {

    public DegreeSpecializationArea(DegreeOfficialPublication officialPublication, MultiLanguageString area) {
        super();
        init(officialPublication, area);
    }

    protected void init(DegreeOfficialPublication degreeOfficialPublication, MultiLanguageString area) {
        checkParameters(degreeOfficialPublication, area);
        setOfficialPublication(degreeOfficialPublication);
        setName(area);
    }

    private void checkParameters(DegreeOfficialPublication degreeOfficialPublication, MultiLanguageString area) {
        if (degreeOfficialPublication == null) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".degreeOfficialPublication.required");
        }
        if (area == null) {
            throw new DomainException(MultiLanguageString.class.getName() + ".area.required");
        }

        if (area.getAllLanguages().isEmpty()) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.required");
        }

        if (!verifyIfSomeContentsAreNotEmpty(area)) {
            throw new DomainException(DegreeSpecializationArea.class.getName() + ".area.names.nameForLanguage.required");
        }
    }

    private boolean verifyIfSomeContentsAreNotEmpty(MultiLanguageString area) {
        for (String language : area.getAllContents()) {
            if (!language.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected RootDomainObject getRootDomainObject() {
        return getOfficialPublication().getRootDomainObject();
    }

    public void delete() {
        removeOfficialPublication();
        deleteDomainObject();
    }

    @Service
    public void setNameEn(String nameEn) {
        this.getName().setContent(Language.en, nameEn);
    }

    @Service
    public void setNamePt(String namePt) {
        this.getName().setContent(Language.pt, namePt);
    }

    public String getNameEn() {
        return this.getName().getContent(Language.en);
    }

    public String getNamePt() {
        return this.getName().getContent(Language.pt);
    }

}
