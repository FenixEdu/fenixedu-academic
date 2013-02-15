package net.sourceforge.fenixedu.presentationTier.renderers;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.utils.RendererPropertyUtils;

public class SimpleAutoCompleteRenderer extends InputRenderer {

    // HACK: this class is haced...
    // all we really need is to override the servlet_uri constant from the AutoCompleteRenderer in the FénixWebFramework.

    public static final String SERVLET_URI = "/ajax/SimpleAutoCompleteServlet";
    public static final String TYPING_VALUE = "custom";

    private String rawSlotName;
    private String valueField;

    private String labelField;
    private String format;

    private String args;
    private Integer maxCount;

    private String size;
    private int minChars;

    private String autoCompleteStyleClass;
    private String autoCompleteItemsStyleClass;
    private String textFieldStyleClass;
    private String errorStyleClass;
    private String autoCompleteWidth;

    public SimpleAutoCompleteRenderer() {
        super();

        setMinChars(3);
    }

    public String getAutoCompleteWidth() {
        return autoCompleteWidth;
    }

    /**
     * Enforces a given width to the auto complete list. If it's not setted then
     * the width will be the same as the input text field.
     * 
     * @param autoCompleteWidth
     */
    public void setAutoCompleteWidth(String autoCompleteWidth) {
        this.autoCompleteWidth = autoCompleteWidth;
    }

    public String getRawSlotName() {
        return this.rawSlotName;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getArgs() {
        return args;
    }

    /**
     * This property allows you to specify a slot that will hold the text that
     * was present in the input field when it was submited.
     * 
     * <p>
     * If the renderer is used to edit the slot <code>slotA</code> of an object A and this property is set the value
     * <code>slotB</code> then when the field is submited the renderer will set the value of the text field in the
     * <code>slotB</code> of the object A.
     * 
     * <p>
     * When you type in the text field an auto completion list is presented. Nevertheless an object is only selected when the user
     * selects one element from the sugested completions. This means that if the user does not select one element after typing
     * some text the value of the slot beeing edited will be set to <code>null</code>.
     * 
     * @property
     */
    public void setRawSlotName(String rawSlotName) {
        this.rawSlotName = rawSlotName;
    }

    public String getLabelField() {
        return this.labelField;
    }

    /**
     * This property allows you tho choose the name of the slot that will be
     * used as the presentation of the object. If this proprty has the value <code>slotL</code> then the list of suggestions will
     * be a list of values
     * obtained by invoking <code>getSlotL</code> in each object.
     * 
     * @property
     */
    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getFormat() {
        return this.format;
    }

    /**
     * Allows you select the presentation format. If not set the value of the
     * field given by {@link #setLabelField(String) labelField} is used. See
     * {@link pt.ist.fenixWebFramework.renderers.utils.RenderUtils#getFormattedProperties(String, Object)} to see the accepted
     * format syntax.
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getMaxCount() {
        return this.maxCount;
    }

    /**
     * Limits the number of results that the servlet returns thus the number of
     * suggestions given to the user.
     * 
     * @property
     */
    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getAutoCompleteItemsStyleClass() {
        return this.autoCompleteItemsStyleClass;
    }

    /**
     * The html class of results shown.
     * 
     * @property
     */
    public void setAutoCompleteItemsStyleClass(String autoCompleteItemsStyleClass) {
        this.autoCompleteItemsStyleClass = autoCompleteItemsStyleClass;
    }

    public String getAutoCompleteStyleClass() {
        return this.autoCompleteStyleClass;
    }

    /**
     * The html class of the container of the results shown.
     * 
     * @property
     */
    public void setAutoCompleteStyleClass(String autoCompleteStyleClass) {
        this.autoCompleteStyleClass = autoCompleteStyleClass;
    }

    public String getTextFieldStyleClass() {
        return this.textFieldStyleClass;
    }

    /**
     * The html class of the text field.
     * 
     * @property
     */
    public void setTextFieldStyleClass(String textFieldStyleClass) {
        this.textFieldStyleClass = textFieldStyleClass;
    }

    public String getErrorStyleClass() {
        return this.errorStyleClass;
    }

    /**
     * The html class of the error message.
     * 
     * @property
     */
    public void setErrorStyleClass(String errorStyleClass) {
        this.errorStyleClass = errorStyleClass;
    }

    public String getSize() {
        return this.size;
    }

    /**
     * The size of the text field.
     * 
     * @property
     */
    public void setSize(String size) {
        this.size = size;
    }

    public int getMinChars() {
        return this.minChars;
    }

    /**
     * The number of characters the user is required to type before any
     * suggestion is offered.
     * 
     * @property
     */
    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new AutoCompleteLayout();
    }

    protected Converter createConverter() {
        return new AutoCompleteConverter();
    }

    protected class AutoCompleteLayout extends Layout {
        public AutoCompleteLayout() {

        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {

            HtmlBlockContainer container = new HtmlBlockContainer();

            addScripts(container);

            MetaSlotKey key = (MetaSlotKey) getContext().getMetaObject().getKey();

            HtmlHiddenField valueField = new HtmlHiddenField();

            valueField.setTargetSlot(key);
            valueField.setId(key.toString() + "_AutoComplete");
            valueField.setName(valueField.getId());
            container.addChild(valueField);

            if (object != null) {
                Object oid = RendererPropertyUtils.getProperty(object, getValueField(), false);
                valueField.setValue(oid == null ? null : oid.toString());
            }

            valueField.setConverter(createConverter());

            HtmlHiddenField oldValueField = new HtmlHiddenField();
            oldValueField.setId(key.toString() + "_OldValue");
            oldValueField.setName(oldValueField.getId());
            container.addChild(oldValueField);

            HtmlTextInput textField = new HtmlTextInput();
            textField.setId(key.toString());
            textField.setName(textField.getId());
            textField.setClasses(getTextFieldStyleClass());
            textField.setSize(getSize());
            textField.setAttribute("autocomplete", "off");
            container.addChild(textField);

            if (object != null && getLabelField() != null) {
                String label = (String) RendererPropertyUtils.getProperty(object, getLabelField(), false);
                textField.setValue(label);
            } else if (getRawSlotName() != null) {
                Object beanObject = getInputContext().getParentContext().getMetaObject().getObject();

                if (beanObject != null) { // protect from a creation context
                    String rawText = (String) RendererPropertyUtils.getProperty(beanObject, getRawSlotName(), false);
                    textField.setValue(rawText);

                    if (rawText != null) {
                        valueField.setValue(TYPING_VALUE);
                    }
                }
            }

            if (getRawSlotName() != null) {
                textField.setController(new UpdateRawNameController(getRawSlotName()));
            }

            HtmlText errorMessage = new HtmlText(RenderUtils.getResourceString("fenix.renderers.autocomplete.error"));
            errorMessage.setId(key.toString() + "_Error");
            errorMessage.setClasses(getErrorStyleClass());
            errorMessage.setStyle("display: none;");
            container.addChild(errorMessage);

            addFinalScript(container, textField.getId());

            return container;
        }

        protected void addScripts(HtmlContainer container) {
            HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setContextRelative(true);

            String[] scriptNames = new String[] { "autoComplete.js", "autoCompleteHandlers.js" };
            for (String script : scriptNames) {
                addSingleScript(container, link, script);
            }
        }

        protected void addSingleScript(HtmlContainer container, HtmlLink link, String scriptName) {
            link.setUrl("/javaScript/" + scriptName);
            HtmlScript script = new HtmlScript("text/javascript", link.calculateUrl(), true);
            container.addChild(script);
        }

        protected void addFinalScript(HtmlContainer container, String textFieldId) {

            HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setContextRelative(true);

            link.setUrl(SERVLET_URI);
            link.setParameter("args", getFormatedArgs());
            link.setParameter("labelField", getLabelField());
            link.setParameter("valueField", getValueField()); // TODO: allow
            // configuration,1
            // needs also
            // converter
            link.setParameter("styleClass", getAutoCompleteItemsStyleClass() == null ? "" : getAutoCompleteItemsStyleClass());
            link.setEscapeAmpersand(false);

            if (getFormat() != null) {
                link.setParameter("format", getFormat());
            }

            if (getMaxCount() != null) {
                link.setParameter("maxCount", String.valueOf(getMaxCount()));
            }

            String finalUri = link.calculateUrl();
            String escapeId = escapeId(textFieldId);
            String scriptText =
                    "jQuery(\"input#"
                            + escapeId
                            + "\").autocomplete(\""
                            + finalUri
                            + "\", { minChars: "
                            + getMinChars()
                            + (!StringUtils.isEmpty(getAutoCompleteWidth()) ? ", width: '" + getAutoCompleteWidth() + "'" : "")
                            + ", validSelection: false"
                            + ", cleanSelection: clearAutoComplete, select: selectElement, after: updateCustomValue, error:showError}); +\n"
                            + "jQuery(\"input[name='" + escapeId + "']\").val(jQuery(\"input#" + escapeId + "\").val());";

            //on submit let's call the updateCustomValue to make sure that the rawSlotName is correctly filled;
            if (getRawSlotName() != null) {
                scriptText =
                        scriptText.concat("\njQuery(\"input[name='" + escapeId + "']\").closest('form').submit(function() {\n"
                                + "var inputFieldVal = jQuery(\"input[name='" + escapeId + "_text']\").val()\n"
                                + "updateRawSlotNameOnSubmit(jQuery(\"input[name='" + escapeId + "_text']\"),inputFieldVal);});");
            }

            HtmlScript script = new HtmlScript();
            script.setContentType("text/javascript");
            script.setScript(scriptText);

            container.addChild(script);
        }

        protected String escapeId(String textFieldId) {
            return textFieldId.replace(".", "\\\\.").replaceAll(":", "\\\\\\\\:");
        }

        protected String getFormatedArgs() {
            Object object = ((MetaSlot) getInputContext().getMetaObject()).getMetaObject().getObject();
            return RenderUtils.getFormattedProperties(getArgs(), object);
        }

    }

    protected static class UpdateRawNameController extends HtmlController {

        private final String rawSlotName;

        public UpdateRawNameController(String rawSlotName) {
            super();

            this.rawSlotName = rawSlotName;
        }

        @Override
        public void execute(IViewState viewState) {
            HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();

            updatRawSlot(viewState, component.getValue());
        }

        private void updatRawSlot(IViewState viewState, String value) {
            Object object = viewState.getMetaObject().getObject();

            try {
                PropertyUtils.setProperty(object, this.rawSlotName, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected static class AutoCompleteConverter extends Converter {

        public AutoCompleteConverter() {
            super();

        }

        @Override
        public Object convert(Class type, Object value) {
            if (value == null || "".equals(value)) {
                return null;
            }

            String text = (String) value;

            if (text.equals(TYPING_VALUE)) {
                return null;
            }

            String key = text;
            return new DomainObjectKeyConverter().convert(type, key);
        }

    }

}
