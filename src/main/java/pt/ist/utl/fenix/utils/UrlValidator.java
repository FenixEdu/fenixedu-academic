/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.utl.fenix.utils;

import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class UrlValidator extends HtmlValidator {

    private static final String[] validSchemes = new String[] { "http", "https" };

    private static final String DEFAULT_SCHEME = "http";

    private boolean required;

    /**
     * Required constructor.
     */
    public UrlValidator() {
        super();
        setKey(true);
        setMessage("renderers.validator.url");
        setRequired(true);
    }

    public UrlValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
        // default messsage
        setKey(true);
        setMessage("renderers.validator.url");
        setRequired(true);
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void performValidation() {
        if (hasValue()) {
            org.apache.commons.validator.routines.UrlValidator urlValidator =
                    new org.apache.commons.validator.routines.UrlValidator(validSchemes);
            setValid(urlValidator.isValid(buildUrlForValidation()));
        } else {
            setValid(!isRequired());
        }
    }

    private boolean hasValue() {
        return (getComponent().getValue() != null && getComponent().getValue().length() > 0);
    }

    private String buildUrlForValidation() {
        String url = getComponent().getValue();
        for (String scheme : validSchemes) {
            if (url.startsWith(scheme)) {
                return url;
            }
        }

        return DEFAULT_SCHEME + "://" + url;
    }
}
