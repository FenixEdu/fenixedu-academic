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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.Properties;

import net.sourceforge.fenixedu.presentationTier.Action.publico.KaptchaAction;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;
import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;
import pt.utl.ist.fenix.tools.util.Pair;

public class CaptchaRenderer extends InputRenderer {

    static private final String DEFAULT_IMAGE_STYLE = "border: 1px solid rgb(187, 187, 187); padding: 5px;";
    static private final String DEFAULT_TEXT_STYLE = "mbottom05";

    private String imageStyle = DEFAULT_IMAGE_STYLE;
    private String textStyle = DEFAULT_TEXT_STYLE;
    private String jcaptchaUrl;

    public String getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getJcaptchaUrl() {
        return jcaptchaUrl;
    }

    public void setJcaptchaUrl(String jcaptchaUrl) {
        this.jcaptchaUrl = jcaptchaUrl;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            protected String getResourceMessage(String message) {
                return RenderUtils.getResourceString("RENDERER_RESOURCES", message);
            }

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                addValidator();

                final MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();
                final HtmlBlockContainer container = new HtmlBlockContainer();

                final HtmlImage image = new HtmlImage();
                image.setSource(getContext().getViewState().getRequest().getContextPath() + getJcaptchaUrl());
                image.setStyle(getImageStyle());
                container.addChild(image);

                final HtmlText text = new HtmlText(getResourceMessage("fenix.renderers.captcha.process"));
                text.setFace(Face.PARAGRAPH);
                text.setClasses(getTextStyle());
                container.addChild(text);

                final HtmlTextInput input = new HtmlTextInput();
                input.setName(key.toString() + "_response");
                input.setTargetSlot(key);
                container.addChild(input);

                return container;
            }

            private void addValidator() {
                final MetaObject metaObject = getInputContext().getMetaObject();
                if (metaObject != null && metaObject instanceof MetaSlot) {
                    final MetaSlot metaSlot = (MetaSlot) metaObject;
                    final Class defaultValidator = CaptchaValidator.class;
                    final Properties properties = new Properties();
                    metaSlot.setValidators(Collections.singletonList(new Pair<Class<HtmlValidator>, Properties>(defaultValidator,
                            properties)));
                }
            }
        };
    }

    static public class CaptchaValidator extends HtmlValidator {

        private static final long serialVersionUID = 5199426957775725692L;

        public CaptchaValidator() {
            super();
            setMessage("renderers.validator.invalid.captcha.value");
        }

        public CaptchaValidator(HtmlChainValidator htmlChainValidator) {
            super(htmlChainValidator);
            setMessage("renderers.validator.invalid.captcha.value");
        }

        @Override
        public void performValidation() {

            final HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();
            final String value = component.getValue();

            if (value == null || value.length() == 0) {
                setMessage("renderers.validator.captcha.required");
                setValid(false);
            } else {
                if (!KaptchaAction.validateResponse(RenderersRequestProcessorImpl.getCurrentRequest().getSession(), value)) {
                    setMessage("renderers.validator.invalid.captcha.value");
                    setValid(false);
                } else {
                    setValid(true);
                }
            }
        }
    }

}
