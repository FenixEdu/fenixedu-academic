package org.fenixedu.academic.domain.degreeStructure;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class CompetenceCourseLevelType extends CompetenceCourseLevelType_Base {

    protected CompetenceCourseLevelType() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static CompetenceCourseLevelType create(final String code, final LocalizedString name) {
        final CompetenceCourseLevelType levelType = new CompetenceCourseLevelType();
        levelType.setCode(code);
        levelType.setName(name);
        return levelType;
    }

    @Override
    public void setCode(String code) {
        if (findByCode(code).filter(level -> level != this).isPresent()) {
            throw new DomainException("error.CompetenceCourseLevelType.setCode.alreadyExistsTypeWithSameCode", code);
        }
        super.setCode(code);
    }

    public static Stream<CompetenceCourseLevelType> findAll() {
        return Bennu.getInstance().getCompetenceCourseLevelTypesSet().stream();
    }

    public static Optional<CompetenceCourseLevelType> findByCode(final String code) {
        return findAll().filter(level -> Objects.equals(level.getCode(), code)).findAny();
    }

    public static Optional<CompetenceCourseLevelType> UNKNOWN() {
        return findByCode("UNKNOWN");
    }

    public void delete() {
        if (!getCompetenceCourseInformationsSet().isEmpty()) {
            throw new DomainException("error.CompetenceCourseLevelType.delete.associatedCompetenceCourseInformationsNotEmpty");
        }

        setRoot(null);
        super.deleteDomainObject();
    }

}
