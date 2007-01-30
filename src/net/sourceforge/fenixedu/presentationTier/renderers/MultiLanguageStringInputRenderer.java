package net.sourceforge.fenixedu.presentationTier.renderers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.presentationTier.renderers.validators.MultiLanguageStringValidator;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlActionLinkController;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * This renderer provides a generic wya of editing slots that contain a {@link MultiLanguageString}.
 * The interface generated allows the user to incrementally add more values in different languages.
 * The user can also remove some of the values already introduced.
 * <p>
 * Example:
 * <div>
 *  <div>
 *      <input type="text"/>
 *      <select>
 *        <option selected="selected" value="">-- Choose an option --</option>
 *        <option value="eo">Esperanto</option>
 *        <option value="xx-klingon">Klingon</option>
 *        <option value="xx-piglatin">Pig Latin</option>
 *        <option value="xx-elmer">Elmer Fudd</option>
 *      </select>
 *      <a href="#">Remove</a>
 *  </div>
 *  <div>
 *      <input type="text"/>
 *      <select>
 *        <option selected="selected" value="">-- Choose an option --</option>
 *        <option value="eo">Esperanto</option>
 *        <option value="xx-klingon">Klingon</option>
 *        <option value="xx-piglatin">Pig Latin</option>
 *        <option value="xx-elmer">Elmer Fudd</option>
 *      </select>
 *      <a href="#">Remove</a>
 *  </div>
 *  <a href="#">Add</a>
 * </div>
 * @author cfgi
 */
public class MultiLanguageStringInputRenderer extends InputRenderer {

    private Integer size;
    
    private String eachClasses;
    
    private String inputClasses;
    
    public Integer getSize() {
        return this.size;
    }

    /**
     * Allows you to configure the size of the input fields for each language.
     * 
     * @property
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * The classes to apply to the div containing each language line.
     *  
     * @property
     */
    public void setEachClasses(String eachClasses) {
        this.eachClasses = eachClasses;
    }

    public String getInputClasses() {
        return this.inputClasses;
    }

    /**
     * The classes to apply to the input field.
     *  
     * @property
     */
    public void setInputClasses(String inputClasses) {
        this.inputClasses = inputClasses;
    }
    
    public static class LanguageBean implements Serializable {
        public Language language;
        public String value;
        
        public LanguageBean(Language language, String value) {
            super();
        
            this.language = language;
            this.value = value;
        }

        public static String exportAsString(LanguageBean languageBean) {
            StringBuilder builder = new StringBuilder();
            
            if (languageBean != null) {
                builder.append(languageBean.language != null ? languageBean.language.toString() : "");
                builder.append(":");
                builder.append(languageBean.value != null ? languageBean.value : "");
            }
            
            return builder.toString();
        }
        
        public static String exportAsString(Collection<LanguageBean> languageBeans) {
            StringBuilder builder = new StringBuilder();
            
            if (languageBeans != null) {
                for (LanguageBean bean : languageBeans) {
                    String beanString = exportAsString(bean);
                    builder.append(beanString.replace("/", "//"));
                    builder.append("/");
                }
            }
            
            return builder.toString();
        }
        
        public static LanguageBean importFromString(String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            
            int firstIndex = value.indexOf(":");
            if (firstIndex == -1) {
                return null;
            }
            
            String language = value.substring(0, firstIndex);
            String message = value.substring(firstIndex + 1);
            
            Language realLanguage = language.length() == 0 ? null : Language.valueOf(language);
            
            return new LanguageBean(realLanguage, message);
        }
        
        public static Collection<LanguageBean> importAllFromString(String value) {
            Collection<LanguageBean> allLanguageBeans = new ArrayList<LanguageBean>();
            
            if (value == null || value.length() == 0) {
                return allLanguageBeans;
            }

            int startIndex = 0;
            int lastIndex = 0;
            int index;

            while (lastIndex < value.length()) {
                index = value.indexOf("/", lastIndex);
                
                if (index == -1) {
                    return allLanguageBeans;
                }
                
                if (index < value.length() - 1 && value.charAt(index + 1) == '/') {
                    lastIndex = index + 2;
                    continue;
                }
                
                String part = value.substring(startIndex, index);
                allLanguageBeans.add(importFromString(part.replace("//", "/")));
                
                lastIndex = index + 1;
                startIndex = lastIndex;
            }
            
            return allLanguageBeans;
        }
    }
    
    protected HtmlSimpleValueComponent getInputComponent() {
        HtmlTextInput textInput = new HtmlTextInput();
        textInput.setSize(getSize() == null ? null : String.valueOf(getSize()));
        
        return textInput;
    }
    
    protected void configureInputComponent(HtmlSimpleValueComponent textInput) {
    }
    
    protected void configureLanguageContainer(HtmlContainer languageContainer, HtmlSimpleValueComponent input, HtmlSimpleValueComponent languageComponent, HtmlActionLink removeLink) {
        languageContainer.addChild(input);
        languageContainer.addChild(languageComponent);
        languageContainer.addChild(removeLink);
    }

    protected Converter getConverter() {
        return new MultiLanguageStringConverter();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        MetaObject metaObject = getInputContext().getMetaObject();
        
        if (metaObject != null && metaObject instanceof MetaSlot) {
            MetaSlot metaSlot = (MetaSlot) metaObject;
            
            if (! metaSlot.hasValidator()) {
                Class defaultValidator = MultiLanguageStringValidator.class;
                metaSlot.setValidator(defaultValidator);
            }
        }
        
        return new MultiLanguageStringInputLayout();
    }

    protected class MultiLanguageStringInputLayout extends Layout {
        private static final String STATE_MAP_NAME = "mlsMap";
        private static final String STATE_INDEX = "lastIndex";

        protected Map<Integer, LanguageBean> getLanguageMap(boolean create) {
            Map<Integer, LanguageBean> map = (Map<Integer, LanguageBean>) getInputContext().getViewState().getLocalAttribute(STATE_MAP_NAME);
            
            if (map == null && create) {
                map = new Hashtable<Integer, LanguageBean>();
                getInputContext().getViewState().setLocalAttribute(STATE_MAP_NAME, map); 
            }
            
            return map;
        }
        
        protected String getLocalName(String part) {
            return getInputContext().getMetaObject().getKey() + "/" + part;
        }
        
        protected Integer updateLastIndex(Integer index) {
            Integer lastIndex = (Integer) getInputContext().getViewState().getLocalAttribute(STATE_INDEX);
            
            if (lastIndex == null || lastIndex < index) {
                lastIndex = index;
                getInputContext().getViewState().setLocalAttribute(STATE_INDEX, index);
            }
            
            return lastIndex;
        }
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            MultiLanguageString mls = (MultiLanguageString) object;
            
            MetaSlotKey key = ((MetaSlot) getInputContext().getMetaObject()).getKey();
            HtmlBlockContainer container = new HtmlBlockContainer();

            // hidden field with real value
            HtmlHiddenField hiddenField = new HtmlHiddenField();
            hiddenField.setTargetSlot(key);
            hiddenField.setController(new MultiLanguageStringController());
            hiddenField.setConverter(getConverter());
            container.addChild(hiddenField);           
            
            // add link
            HtmlActionLink addLink = new HtmlActionLink(RenderUtils.getResourceString("renderers.language.add"));
            addLink.setName(getLocalName("add"));
            container.addChild(addLink);
            
            Map<Integer, LanguageBean> map = getLanguageMap(false);
            if ((map == null || map.isEmpty()) && (mls != null && !mls.isEmpty())) {
                map = getLanguageMap(true);
                
                int index = 0;
                for (Language language : mls.getAllLanguages()) {
                    map.put(index++, new LanguageBean(language, mls.getContent(language)));
                }
            }
            
            HtmlActionLink firstRemoveLink = null;
            HtmlActionLink secondRemoveLink = null;
            
            if (map != null) {
                List<Map.Entry<Integer, LanguageBean>> list = new ArrayList<Map.Entry<Integer, LanguageBean>>(map.entrySet());
                Collections.sort(list, new Comparator<Map.Entry<Integer, LanguageBean>>() {

                    public int compare(Entry<Integer, LanguageBean> o1, Entry<Integer, LanguageBean> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    
                });
                
                for (Map.Entry<Integer, LanguageBean> entry : list) {
                    HtmlActionLink link = addLanguageInput(container, entry.getKey(), entry.getValue().value, entry.getValue().language, list.size() > 1);
                    
                    if (firstRemoveLink == null) {
                        firstRemoveLink = link;
                    }
                    else if (secondRemoveLink == null) {
                        secondRemoveLink = link;
                    }
                }
            }
            else {
                // default: present one entry without allowing to remove
                addLanguageInput(container, 0, "", null, false);
            }

            // setup controllers to avoid displaying the remove link when only one line is present
            addLink.setController(new AddNewLanguageController(container, firstRemoveLink));
            if (map != null && map.size() == 2) {
                ((RemoveLanguageController) firstRemoveLink.getController()).setLink(secondRemoveLink);
                ((RemoveLanguageController) secondRemoveLink.getController()).setLink(firstRemoveLink);
            }
            
            return container;
        }
        
        private HtmlActionLink addLanguageInput(HtmlContainer container, Integer index, String value, Language language, boolean allowRemove) {
            // insert empty entry if not present
            Map<Integer, LanguageBean> map = getLanguageMap(true);
            if (! map.containsKey(index)) {
                map.put(index, new LanguageBean(null, null));
            }
            
            updateLastIndex(index);

            // create component line
            HtmlContainer inputContainer = new HtmlBlockContainer();
            inputContainer.setClasses(getEachClasses());
            
            HtmlSimpleValueComponent textInput = getInputComponent();
            textInput.setClasses(getInputClasses());
            
            textInput.setName(getLocalName("text/" + index));
            textInput.setValue(value);

            configureInputComponent(textInput);
            
            PresentationContext context = getInputContext().createSubContext(getInputContext().getMetaObject());
            context.setProperties(new Properties());
            
            Language usedLanguage = language == null ? LanguageUtils.getUserLanguage() : language;
            
            HtmlSimpleValueComponent languageComponent = (HtmlSimpleValueComponent) RenderKit.getInstance().render(context, usedLanguage, Language.class);
            languageComponent.setController(new UpdateLanguageController(textInput, index));
            languageComponent.setTargetSlot(null);
            languageComponent.setName(getLocalName("language/" + index));

            HtmlActionLink removeLink = new HtmlActionLink(RenderUtils.getResourceString("renderers.language.remove"));
            removeLink.setVisible(allowRemove);
            removeLink.setName(getLocalName("remove/" + index));
            removeLink.setController(new RemoveLanguageController(container, inputContainer, index));
            
            configureLanguageContainer(inputContainer, textInput, languageComponent, removeLink);

            container.getChildren().add(container.getChildren().size() - 2, inputContainer);
            
            return removeLink;
        }

        private class MultiLanguageStringController extends HtmlController {
            @Override
            public void execute(IViewState viewState) {
                String value = null;
                Map<Integer, LanguageBean> map = getLanguageMap(false);
                
                if (map != null && map.size() > 0) {
                    value = LanguageBean.exportAsString(map.values());
                }
                
                HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
                component.setValue(value == null ? null : value.toString());
            }
        }

        private class UpdateLanguageController extends HtmlController {
            private final HtmlSimpleValueComponent input;
            private final Integer index;

            private UpdateLanguageController(HtmlSimpleValueComponent textInput, Integer index) {
                super();
                
                this.input = textInput;
                this.index = index;
            }

            @Override
            public void execute(IViewState viewState) {
                Map<Integer, LanguageBean> map = getLanguageMap(true);
            
                HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();

                String value = this.input.getValue();
                Language finalLanguage = component.getValue() == null || component.getValue().length() == 0 ? null : Language.valueOf(component.getValue());
                
                map.put(this.index, new LanguageBean(finalLanguage, value));
            }
        }

        private class AddNewLanguageController extends HtmlActionLinkController {

            private HtmlBlockContainer container;
            private HtmlActionLink link;

            public AddNewLanguageController(HtmlBlockContainer container, HtmlActionLink link) {
                this.container = container;
                this.link = link;
            }

            @Override
            public void linkPressed(IViewState viewState, HtmlActionLink link) {
                viewState.setSkipValidation(true);
                
                Integer index = updateLastIndex(0);
                updateLastIndex(index++);

                if (this.link != null) {
                    this.link.setVisible(true);
                }
                
                addLanguageInput(this.container, index, "", null, true);
            }
            
        }

        private class RemoveLanguageController extends HtmlActionLinkController {

            private HtmlContainer container;
            private HtmlContainer inputContainer;
            private HtmlActionLink link;
            private Integer index;

            public RemoveLanguageController(HtmlContainer container, HtmlContainer inputContainer, Integer index) {
                this.container = container;
                this.inputContainer = inputContainer;
                this.index = index;
            }

            public void setLink(HtmlActionLink link) {
                this.link = link;
            }

            @Override
            public void linkPressed(IViewState viewState, HtmlActionLink link) {
                viewState.setSkipValidation(true);
                
                this.container.removeChild(this.inputContainer);
                if (this.link != null) {
                    this.link.setVisible(false);
                }

                Map<Integer, LanguageBean> map = getLanguageMap(true);
                map.remove(this.index);
            }
            
        }
        
    }

    public static class MultiLanguageStringConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            String text = (String) value;

            MultiLanguageString mls = new MultiLanguageString();
            
            Collection<LanguageBean> allLanguageBean = LanguageBean.importAllFromString(text);
            for (LanguageBean bean : allLanguageBean) {
                if (bean.value != null && bean.value.trim().length() != 0) {
                    mls.setContent(bean.language, bean.value);
                }
            }
            
            return mls;
        }
        
    }

}
