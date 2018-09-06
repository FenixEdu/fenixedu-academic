package org.fenixedu.academic.ui.renderers.validators;

import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;
import org.fenixedu.academic.util.Bundle;

import pt.ist.fenixWebFramework.renderers.validators.RequiredValidator;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class SocialSecurityNumberValidator extends RequiredValidator {


    public SocialSecurityNumberValidator() {
        setKey(true);
        setMessage("label.person.details.vatNumber.invalid");
        setBundle(Bundle.APPLICATION);
    }


    @Override
    public void performValidation() {
        setValid(PartySocialSecurityNumber.isValid(getComponent().getValue()));
    }

}
