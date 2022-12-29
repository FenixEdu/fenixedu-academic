package org.fenixedu.academic.domain.student.registrationStates;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class RegistrationStateType extends RegistrationStateType_Base {

    public static final String REGISTERED_CODE = "REGISTERED";
    public static final String CONCLUDED_CODE = "CONCLUDED";

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

    public boolean isRegistered() {
        return REGISTERED_CODE.equals(getCode());
    }

    public boolean isConcluded() {
        return CONCLUDED_CODE.equals(getCode());
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

}
