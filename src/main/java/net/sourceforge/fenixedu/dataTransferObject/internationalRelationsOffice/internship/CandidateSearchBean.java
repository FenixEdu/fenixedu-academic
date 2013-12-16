package net.sourceforge.fenixedu.dataTransferObject.internationalRelationsOffice.internship;

import java.io.Serializable;
import java.util.Collections;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author Pedro Santos (pmrsa)
 */
public class CandidateSearchBean implements Serializable {
    public static class SessionProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            return Bennu.getInstance().getInternshipCandidacySessionSet();
        }
    }

    public static class UniversitiesProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            CandidateSearchBean bean = (CandidateSearchBean) source;
            if (bean.getSession() != null) {
                return bean.getSession().getUniversitySet();
            }
            return Collections.emptySet();
        }
    }

    private static final long serialVersionUID = 1170872005958537842L;

    private InternshipCandidacySession session;

    private AcademicalInstitutionUnit university;

    private LocalDate cutStart;

    private LocalDate cutEnd;

    public InternshipCandidacySession getSession() {
        return session;
    }

    public void setSession(InternshipCandidacySession session) {
        this.session = session;
    }

    public AcademicalInstitutionUnit getUniversity() {
        return university;
    }

    public void setUniversity(AcademicalInstitutionUnit university) {
        this.university = university;
    }

    public LocalDate getCutStart() {
        return cutStart;
    }

    public void setCutStart(LocalDate cutStart) {
        this.cutStart = cutStart;
    }

    public LocalDate getCutEnd() {
        return cutEnd;
    }

    public void setCutEnd(LocalDate cutEnd) {
        this.cutEnd = cutEnd;
    }

    public String getName() {
        StringBuilder output = new StringBuilder();
        if (getUniversity() != null) {
            output.append(getUniversity().getFullPresentationName().replace(' ', '.'));
        } else {
            output.append("todos");
        }
        output.append(".");
        if (getCutStart() != null) {
            output.append(getCutStart().toString("dd.MM.yy"));
        }
        output.append("-");
        if (getCutEnd() != null) {
            output.append(getCutEnd().toString("dd.MM.yy"));
        }
        return output.toString();
    }
}
