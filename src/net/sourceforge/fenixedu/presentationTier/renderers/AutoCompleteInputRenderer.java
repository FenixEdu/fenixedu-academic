package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This renderer allows you to search for a domain object by providing a list
 * of possible completions, for the text typed, using a javascript technique.
 * 
 * <p>
 * This renderer can be used to do the input of any domain object because you
 * can configure the concrete service that searches the objects and all objects
 * are referred to by their internal id. It's recommended that a specialized
 * service is created for most cases so that it's as efficient as possible.
 * 
 * <p>
 * Example: <br/>
 * <input type="text" value="po" style="width: 20em;"/>
 * <div style="margin-top: -10px; border: 1px solid #eee; width: 20em;">
 *  <ul>
 *      <li>French Polynesia</li>
 *      <li>Poland</li>
 *      <li>Portugal</li>
 *      <li>Singapore</li>
 *  </ul>
 * </div>
 * 
 * @author cfgi
 */
public class AutoCompleteInputRenderer extends InputRenderer {

    public static final String SERVLET_URI  = "/ajax/AutoCompleteServlet";
    public static final String TYPING_VALUE  = "custom";
    
    private String rawSlotName;
    
    private String labelField;
    private String format;

    private String className;
    private String serviceName;
    private String serviceArgs;

    private Integer maxCount;
    
    private String size;
    private int minChars;
    
    private String autoCompleteStyleClass;
    private String autoCompleteItemsStyleClass;
    private String textFieldStyleClass;
    private String errorStyleClass;
    
    private boolean indicatorShown;
    
    public AutoCompleteInputRenderer() {
        super();
        
        setIndicatorShown(true);
        setMinChars(3);
    }

    public String getRawSlotName() {
        return this.rawSlotName;
    }

    /**
     * This property allows you to specify a slot that will hold the text that
     * was present in the input field when it was submited.
     *
     * <p>
     * If the renderer is used to edit the slot <code>slotA</code> of an object A
     * and this property is set the value <code>slotB</code> then when the field
     * is submited the renderer will set the value of the text field in the 
     * <code>slotB</code> of the object A.
     * 
     * <p>
     * When you type in the text field an auto completion list is presented.
     * Nevertheless an object is only selected when the user selects one element
     * from the sugested completions. This means that if the user does not
     * select one element after typing some text the value of the slot beeing
     * edited will be set to <code>null</code>.
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
     * This property allows you tho choose the name of the slot that will be used
     * as the presentation of the object. If this proprty has the value <code>slotL</code>
     * then the list of suggestions will be a list of values obtained by invoking
     * <code>getSlotL</code> in each object. 
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
     * Allows you select the presentation format. If not set the value of the field given
     * by {@link #setLabelField(String) labelField} is used. See {@link net.sourceforge.fenixedu.renderers.utils.RenderUtils#getFormattedProperties(String, Object)}
     * to see the accepted format syntax. 
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * Configures the service that should be used to do the search. That service must
     * implement the interface {@link net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService}.
     * 
     * @property
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceArgs() {
        return this.serviceArgs;
    }

    /**
     * Allows you to pass extra arguments to the service in the form 
     * <code>paramA=value1,paramB=value2</code>. This arguments will
     * be available in the arguments map passed to the service.
     * 
     * @property
     */
    public void setServiceArgs(String serviceArgs) {
        this.serviceArgs = serviceArgs;
    }

    public Integer getMaxCount() {
        return this.maxCount;
    }

    /**
     * Limits the number of results that the servlet returns thus the
     * number of suggestions given to the user.
     * 
     * @property
     */
    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getClassName() {
        if (this.className != null) {
            return this.className;            
        }
        else {
            return getContext().getMetaObject().getType().getName();
        }
    }

    /**
     * The name of the type of objects we want to search. This should be
     * the the same type or a subtype of the type of the slot this 
     * rendering is editing.
     * 
     * @property
     */
    public void setClassName(String className) {
        this.className = className;
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
     * The number of characters the user is required to type before any suggestion is offered.
     * 
     * @property
     */
    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    public boolean isIndicatorShown() {
        return this.indicatorShown;
    }

    /**
     * When this property is set to <code>true</code> a progress indicator is shown
     * during the comunication with the server. 
     * 
     * @property
     */
    public void setIndicatorShown(boolean indicatorShown) {
        this.indicatorShown = indicatorShown;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                
                HtmlInlineContainer container = new HtmlInlineContainer();

                addScripts(container);
                
                MetaSlotKey key = (MetaSlotKey) getContext().getMetaObject().getKey();

                HtmlHiddenField valueField = new HtmlHiddenField();
                valueField.setTargetSlot(key);
                valueField.setId(key.toString() + "_AutoComplete");
                valueField.setName(valueField.getId());
                container.addChild(valueField);
                
                if (object != null) {
                    Object idInternal = RendererPropertyUtils.getProperty(object, "idInternal", false);
                    valueField.setValue(idInternal == null ? null : idInternal.toString());
                }

                valueField.setConverter(new AutoCompleteConverter(getClassName()));
                
                HtmlHiddenField oldValueField = new HtmlHiddenField();
                oldValueField.setId(key.toString() + "_OldValue");
                oldValueField.setName(oldValueField.getId());
                container.addChild(oldValueField);
                
                HtmlTextInput textField = new HtmlTextInput();
                textField.setId(key.toString());
                textField.setName(textField.getId());
                textField.setClasses(getTextFieldStyleClass());
                textField.setSize(getSize());
                textField.setOnKeyDown("javascript:autoCompleteKeyDownHandler(event, '" + textField.getId() +"');");
                textField.setOnKeyUp("javascript:autoCompleteKeyUpHandler(event, '" + textField.getId() + "', '" + TYPING_VALUE + "');");
                container.addChild(textField);

                if (object != null && getLabelField() != null) {
                    String label = (String) RendererPropertyUtils.getProperty(object, getLabelField(), false);
                    textField.setValue(label);
                }
                else if (getRawSlotName() != null) {
                    Object beanObject = getInputContext().getMetaObject().getObject();
                    String rawText = (String) RendererPropertyUtils.getProperty(beanObject, getRawSlotName(), false);
                    textField.setValue(rawText);
                }
                
                if (getRawSlotName() != null) {
                    textField.setController(new UpdateRawNameController(getRawSlotName()));
                }
                
                HtmlInlineContainer loadingContainer = new HtmlInlineContainer();
                loadingContainer.setId(key.toString() + "_Indicator");
                loadingContainer.setStyle("display: none;");

                HtmlText loadingText = new HtmlText(RenderUtils.getResourceString("fenix.renderers.autocomplete.loading"));
                loadingContainer.addChild(loadingText);
                
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setUrl("/images/autocomplete/spinner.gif");

                HtmlImage indicatorImage = new HtmlImage();
                indicatorImage.setSource(link.calculateUrl());
                loadingContainer.addChild(indicatorImage);

                if (isIndicatorShown()) {
                    container.addChild(loadingContainer);
                }

                HtmlText errorMessage = new HtmlText(RenderUtils.getResourceString("fenix.renderers.autocomplete.error"));
                errorMessage.setId(key.toString() + "_Error");
                errorMessage.setClasses(getErrorStyleClass());
                errorMessage.setStyle("display: none;");
                container.addChild(errorMessage);
                
                HtmlBlockContainer resultsContainer = new HtmlBlockContainer();
                resultsContainer.setId(key.toString() + "_div");
                resultsContainer.setClasses(getAutoCompleteStyleClass());
                container.addChild(resultsContainer);
                
                addFinalScript(container, textField.getId(), resultsContainer.getId(), loadingContainer.getId());
                
                return container;
            }

            private void addScripts(HtmlInlineContainer container) {
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setContextRelative(true);
                
                String[] scriptNames = new String[] { 
                        "prototype.js", 
                        "effects.js", 
                        "dragdrop.js", 
                        "controls.js", 
                        "fenixScript.js" 
                };
                
                for (String script : scriptNames) {
                    addSingleScript(container, link, script);
                }
            }

            private void addSingleScript(HtmlInlineContainer container, HtmlLink link, String scriptName) {
                link.setUrl("/javaScript/" + scriptName);
                HtmlScript script = new HtmlScript("text/javascript", link.calculateUrl(), true);
                container.addChild(script);
            }

            private void addFinalScript(HtmlInlineContainer container, String textFieldId, String divId, String indicatorId) {
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setContextRelative(true);
                
                link.setUrl(SERVLET_URI);
                link.setParameter("serviceName", getServiceName());
                link.setParameter("serviceArgs", getServiceArgs());
                link.setParameter("labelField", getLabelField());
                link.setParameter("valueField", "idInternal"); // TODO: allow configuration, needs also converter
                link.setParameter("styleClass", getAutoCompleteItemsStyleClass() == null ? "" : getAutoCompleteItemsStyleClass());
                link.setParameter("class", getClassName());
                
                if (getFormat() != null) {
                    link.setParameter("format", getFormat());
                }
                
                if (getMaxCount() != null) {
                    link.setParameter("maxCount", String.valueOf(getMaxCount()));
                }
                
                String finalUri = link.calculateUrl();
                String scriptText = "new Ajax.Autocompleter('" + textFieldId + "','" + divId + "','" + finalUri +
                        "', {paramName: 'value', afterUpdateElement: autoCompleteUpdate, minChars: " + getMinChars() +
                        (isIndicatorShown() ? ", indicator: '" + indicatorId + "'" : "") + 
                        ", onFailure: function (transport) { showAutoCompleteError('" + textFieldId + "'); }});";
                
                HtmlScript script = new HtmlScript();
                script.setContentType("text/javascript");
                script.setScript(scriptText);
                
                container.addChild(script);
            }

        };
    }

    private static class UpdateRawNameController extends HtmlController {

        private String rawSlotName;

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
    
    private static class AutoCompleteConverter extends Converter {

        private String className;

        public AutoCompleteConverter(String className) {
            super();
            
            this.className = className;
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
            
            String key = DomainObjectKeyConverter.code(this.className, text);
            return new DomainObjectKeyConverter().convert(type, key);
        }
        
    }

}
