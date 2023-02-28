package org.fenixedu.academic.domain.degreeStructure;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class CourseLoadType extends CourseLoadType_Base {

    public static final String THEORETICAL = "THEORETICAL";
    public static final String THEORETICAL_PRACTICAL = "THEORETICAL_PRACTICAL";
    public static final String PRACTICAL_LABORATORY = "PRACTICAL_LABORATORY";
    public static final String FIELD_WORK = "FIELD_WORK";
    public static final String SEMINAR = "SEMINAR";
    public static final String INTERNSHIP = "INTERNSHIP";
    public static final String TUTORIAL_ORIENTATION = "TUTORIAL_ORIENTATION";
    public static final String OTHER = "OTHER";
    public static final String AUTONOMOUS_WORK = "AUTONOMOUS_WORK";

    protected CourseLoadType() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static CourseLoadType create(final String code, final LocalizedString name, final LocalizedString initials,
            final boolean allowShifts) {
        final CourseLoadType result = new CourseLoadType();
        result.setCode(code);
        result.setName(name);
        result.setInitials(initials);
        result.setAllowShifts(allowShifts);
        result.setDisplayOrder((int) findAll().count());
        return result;
    }

    public static Stream<CourseLoadType> findAll() {
        return Bennu.getInstance().getCourseLoadTypesSet().stream();
    }

    public static Optional<CourseLoadType> findByCode(final String code) {
        return findAll().filter(type -> Objects.equals(type.getCode(), code)).findAny();
    }

    @Override
    public void setCode(String code) {
        if (findByCode(code).filter(type -> type != this).isPresent()) {
            throw new IllegalArgumentException("CourseLoadType already exists with same code");
        }

        super.setCode(code);
    }

    @Override
    public void setDisplayOrder(int displayOrder) {
        if (findAll().filter(type -> type != this).anyMatch(type -> type.getDisplayOrder() == displayOrder)) {
            throw new IllegalArgumentException("CourseLoadType already exists with same display order");
        }

        super.setDisplayOrder(displayOrder);
    }

    public void delete() {
        if (!getCourseLoadDurationsSet().isEmpty()) {
            throw new IllegalStateException("CourseLoadType cannot be deleted, because it has CourseLoadDurations");
        }

        setRoot(null);
        super.deleteDomainObject();
    }

}
