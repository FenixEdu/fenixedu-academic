package org.fenixedu.academic.domain.student.registrationStates;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class RegistrationStateType extends RegistrationStateType_Base {

    protected RegistrationStateType() {
        super();
        setRoot(Bennu.getInstance());
    }

    public static RegistrationStateType create(final String code, final LocalizedString name, final boolean active,
            final RegistrationStateTypeEnum typeEnum) {
        final RegistrationStateType result = new RegistrationStateType();
        result.setCode(code);
        result.setName(name);
        result.setActive(active);
        result.setTypeEnum(typeEnum);
        return result;
    }

    @Override
    public void setCode(String code) {
        if (findByCode(code).filter(type -> type != this).isPresent()) {
            throw new IllegalStateException("RegistrationStateType already exists with same number");
        }

        super.setCode(code);
    }

    public static Stream<RegistrationStateType> findAll() {
        return Bennu.getInstance().getRegistrationStateTypesSet().stream();
    }

    public static Optional<RegistrationStateType> findByCode(final String code) {
        return findAll().filter(type -> Objects.equals(type.getCode(), code)).findAny();
    }

    public void delete() {
        setRoot(null);
        super.deleteDomainObject();
    }

    public static void bootstrap() {
        Stream.of(RegistrationStateTypeEnum.values()).forEach(typeEnum -> findByCode(typeEnum.getName()).orElseGet(() -> {

            final String code = typeEnum.getName();
            final LocalizedString name = BundleUtil.getLocalizedString(Bundle.ENUMERATION, typeEnum.getQualifiedName());
            final boolean active = typeEnum.isActive();

            return create(code, name, active, typeEnum);
        }));
    }

}
