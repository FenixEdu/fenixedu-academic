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
package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class TextLengthValidator extends HtmlValidator {

    private static final Logger logger = LoggerFactory.getLogger(TextLengthValidator.class);

    public static enum TextType {
        character, word, paragraph
    }

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private static final String LENGTH_MESSAGE = "fenix.renderers.length.exceeded";

    private TextType type;
    private Integer length;

    public TextLengthValidator() {
        super();
        setType("character");
    }

    public TextLengthValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);

        setType("character");
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(String type) {
        try {
            this.type = TextType.valueOf(type);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void performValidation() {

        if (isValid() && getLength() != null) {
            String value = getComponent().getValue();

            if (value != null) {
                validateLength(value);
            }
        }
    }

    private void validateLength(String value) {
        if (getLength() == null) {
            return;
        }

        if (getCount(value) > getLength()) {
            invalidate();
        }
    }

    private int getCount(String value) {
        switch (this.type) {
        case character:
            return value.length();
        case word:
            return value.split("\\p{Space}+").length;
        default:
            return 0;
        }
    }

    private void invalidate() {
        setValid(false);
        setMessage(LENGTH_MESSAGE + "." + getType());
    }

    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getFormatedResourceString(message, getLength());
    }

}
