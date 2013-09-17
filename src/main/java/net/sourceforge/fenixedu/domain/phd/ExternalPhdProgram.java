package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExternalPhdProgram extends ExternalPhdProgram_Base {

    public ExternalPhdProgram() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
        setWhenCreated(new DateTime());
    }

    private ExternalPhdProgram(String name, String nameEn, String acronym,
            PhdIndividualProgramCollaborationType forCollaborationType) {
        this();

        check(name, nameEn, acronym, forCollaborationType);

        MultiLanguageString nameI18N = new MultiLanguageString(Language.pt, name).with(Language.en, nameEn);

        setName(nameI18N);

        setAcronym(acronym);
        setForCollaborationType(forCollaborationType);
    }

    private void check(String name, String nameEn, String acronym, PhdIndividualProgramCollaborationType forCollaborationType) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.external.phd.program.name.is.empty");
        }

        if (StringUtils.isEmpty(nameEn)) {
            throw new DomainException("error.external.phd.program.name.en.is.empty");
        }

        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.external.phd.program.acronym.is.empty");
        }

        if (forCollaborationType == null) {
            throw new DomainException("error.external.phd.program.for.collaboration.type.is.null");
        }
    }

    public boolean isForCollaborationType(PhdIndividualProgramCollaborationType type) {
        return getForCollaborationType() == type;
    }

    public static ExternalPhdProgram createExternalPhdProgram(String name, String nameEn, String acronym,
            PhdIndividualProgramCollaborationType forCollaborationType) {
        if (readExternalPhdProgramByName(name) != null) {
            throw new DomainException("error.external.phd.program.with.same.name.exists");
        }

        if (readExternalPhdProgramByAcronym(acronym) != null) {
            throw new DomainException("error.external.phd.program.with.same.acronym.exists");
        }

        return new ExternalPhdProgram(name, nameEn, acronym, forCollaborationType);
    }

    public static List<ExternalPhdProgram> readExternalPhdProgramsForCollaborationType(
            final PhdIndividualProgramCollaborationType type) {
        List<ExternalPhdProgram> phdProgramList = new ArrayList<ExternalPhdProgram>();

        CollectionUtils.select(RootDomainObject.getInstance().getExternalPhdPrograms(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((ExternalPhdProgram) arg0).isForCollaborationType(type);
            }

        }, phdProgramList);

        return phdProgramList;
    }

    public static ExternalPhdProgram readExternalPhdProgramByName(final String name) {
        return (ExternalPhdProgram) CollectionUtils.find(RootDomainObject.getInstance().getExternalPhdPrograms(),
                new Predicate() {

                    @Override
                    public boolean evaluate(Object object) {
                        return name.equals(((ExternalPhdProgram) object).getName().getContent());
                    }

                });
    }

    public static ExternalPhdProgram readExternalPhdProgramByAcronym(final String acronym) {
        return (ExternalPhdProgram) CollectionUtils.find(RootDomainObject.getInstance().getExternalPhdPrograms(),
                new Predicate() {

                    @Override
                    public boolean evaluate(Object object) {
                        return acronym.equals(((ExternalPhdProgram) object).getAcronym());
                    }

                });
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess> getIndividualProgramProcesses() {
        return getIndividualProgramProcessesSet();
    }

    @Deprecated
    public boolean hasAnyIndividualProgramProcesses() {
        return !getIndividualProgramProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea> getPhdProgramFocusAreas() {
        return getPhdProgramFocusAreasSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramFocusAreas() {
        return !getPhdProgramFocusAreasSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasForCollaborationType() {
        return getForCollaborationType() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

}
