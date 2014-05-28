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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import pt.ist.fenixWebFramework.rendererExtensions.MultiLanguageStringInputRenderer.LanguageBean;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class MultiLanguageStringValidator extends HtmlValidator {

    public MultiLanguageStringValidator() {
        super();
    }

    public MultiLanguageStringValidator(HtmlChainValidator htmlChainValidator) {
        super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

        Collection<LanguageBean> beans = LanguageBean.importAllFromString(component.getValue());

        boolean hasRepeatedLanguage = false;
        boolean hasNullLanguage = false;

        List<Locale> languages = new ArrayList<Locale>();

        for (LanguageBean bean : beans) {
            // only consider fields not empty
            if (bean.value != null && bean.value.trim().length() > 0) {
                if (bean.language == null) {
                    hasNullLanguage = true;
                } else if (languages.contains(bean.language)) {
                    hasRepeatedLanguage = true;
                } else {
                    languages.add(bean.language);
                }
            }
        }

        if (hasRepeatedLanguage) {
            invalidate("renderers.validator.language.repeated");
            return;
        }

        if (hasNullLanguage) {
            invalidate("renderers.validator.language.null");
            return;
        }

        setValid(true);
    }

    private void invalidate(String message) {
        setValid(false);
        setMessage(message);
    }

}
