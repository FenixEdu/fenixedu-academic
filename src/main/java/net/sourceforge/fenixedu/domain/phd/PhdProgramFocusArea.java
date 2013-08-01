package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdProgramFocusArea extends PhdProgramFocusArea_Base {
    static public Comparator<PhdProgramFocusArea> COMPARATOR_BY_NAME = new Comparator<PhdProgramFocusArea>() {
        @Override
        public int compare(PhdProgramFocusArea o1, PhdProgramFocusArea o2) {
            int result = o1.getName().compareTo(o2.getName());
            return (result != 0) ? result : DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }
    };

    private PhdProgramFocusArea() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    public PhdProgramFocusArea(final MultiLanguageString name) {
        this();
        String[] args = {};
        if (name == null) {
            throw new DomainException("error.PhdProgramFocusArea.invalid.name", args);
        }
        checkForEqualFocusArea(name);
        setName(name);
    }

    private void checkForEqualFocusArea(final MultiLanguageString name) {
        for (final PhdProgramFocusArea focusArea : Bennu.getInstance().getPhdProgramFocusAreasSet()) {
            if (focusArea != this && focusArea.getName().equalInAnyLanguage(name)) {
                throw new DomainException("error.PhdProgramFocusArea.found.area.with.same.name");
            }
        }
    }

    public void delete() {
        getPhdPrograms().clear();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean hasPhdProgramFor(final Degree degree) {
        for (final PhdProgram program : getPhdProgramsSet()) {
            if (program.hasDegree() && program.getDegree().equals(degree)) {
                return true;
            }
        }
        return false;
    }

    public List<ExternalPhdProgram> getAssociatedExternalPhdProgramsForCollaborationType(
            final PhdIndividualProgramCollaborationType type) {
        List<ExternalPhdProgram> externalPhdProgramList = new ArrayList<ExternalPhdProgram>();

        CollectionUtils.select(getExternalPhdPrograms(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return ((ExternalPhdProgram) object).isForCollaborationType(type);
            }

        }, externalPhdProgramList);

        return externalPhdProgramList;
    }

    public static PhdProgramFocusArea readPhdProgramFocusAreaByName(final String name) {
        return (PhdProgramFocusArea) CollectionUtils.find(Bennu.getInstance().getPhdProgramFocusAreasSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return name.equals(((PhdProgramFocusArea) arg0).getName().getContent());
            }

        });
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgram> getPhdPrograms() {
        return getPhdProgramsSet();
    }

    @Deprecated
    public boolean hasAnyPhdPrograms() {
        return !getPhdProgramsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ExternalPhdProgram> getExternalPhdPrograms() {
        return getExternalPhdProgramsSet();
    }

    @Deprecated
    public boolean hasAnyExternalPhdPrograms() {
        return !getExternalPhdProgramsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubject> getThesisSubjects() {
        return getThesisSubjectsSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjects() {
        return !getThesisSubjectsSet().isEmpty();
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
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

}
