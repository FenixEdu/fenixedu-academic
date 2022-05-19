package org.fenixedu.academic.domain.degreeStructure;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class CompetenceCourseLevelType extends CompetenceCourseLevelType_Base {

    private static final Logger LOG = LoggerFactory.getLogger(CompetenceCourseLevelType.class);

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

    @Atomic
    public static void bootstrap() {
        if (findAll().findAny().isEmpty()) {
            LOG.info("CompetenceCourseLevelType Bootstrap START");

            LOG.info("Creating level types");
            Stream.of(CompetenceCourseLevel.values()).forEach(ccl -> {
                final LocalizedString name =
                        new LocalizedString.Builder().with(Locale.getDefault(), ccl.getLocalizedName(Locale.getDefault()))
                                .with(Locale.ENGLISH, ccl.getLocalizedName(Locale.ENGLISH)).build();
                CompetenceCourseLevelType.create(ccl.name(), name);
            });

            LOG.info("Updating competence course informations");
            Bennu.getInstance().getCompetenceCourseInformationsSet()
                    .forEach(cci -> cci.setCompetenceCourseLevel(cci.getCompetenceCourseLevel()));

            LOG.info("CompetenceCourseLevelType Bootstrap END");
        }
    }

}
