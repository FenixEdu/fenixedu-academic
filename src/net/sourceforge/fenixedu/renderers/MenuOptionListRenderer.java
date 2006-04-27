package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer as a purpose similar to {@link net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer}
 * but is intended to collect only one value. All the possible values for the slot beeing edited
 * are presented in an html menu. The presentation of each object must have in consideration
 * that the object is beeing presented in an option of the menu so it must be short and simple.
 * If possible used the <tt>format</tt> property to format the object. Nevertheless the usual 
 * configuration is possible with <tt>eachLayout</tt> and <tt>eachSchema</tt>.
 * 
 * <p>
 * Example:
 * <select>
 *  <option>&lt;object A presentation&gt;</option>
 *  <option>&lt;object B presentation&gt;</option>
 *  <option>&lt;object C presentation&gt;</option>
 * </select>
 * 
 * @author cfgi
 */
public class MenuOptionListRenderer extends InputRenderer {
    private String format; 
    
    private String eachSchema;

    private String eachLayout;
    
    private String providerClass;

    private DataProvider provider;
    
    public String getFormat() {
        return this.format;
    }

    /**
     * This allows to specify a presentation format for each
     * object. For more details about the format syntaxt
     * check the {@see FormatRenderer}. 
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getEachLayout() {
        return this.eachLayout;
    }

    /**
     * The layout to be used when presenting each object. This property will
     * only be used if {@link #setFormat(String) format} is not specified. 
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return this.eachSchema;
    }

    /**
     * The schema to be used when presenting each object.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getProviderClass() {
        return this.providerClass;
    }

    /**
     * The class name of a {@linkplain DataProvider data provider}. The
     * provider is responsible for constructing a collection will all
     * possible values.
     * 
     * @property
     */
    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new MenuOptionLayout();
    }

    protected DataProvider getProvider() {
        if (this.provider == null) {
            String className = getProviderClass();
            
            try {
                Class providerCass = (Class<DataProvider>) Class.forName(className);
                this.provider = (DataProvider) providerCass.newInstance();  
            } catch (Exception e) {
                throw new RuntimeException("could not get a data provider instance", e);
            }
        }
        
        return this.provider;
    }
    
    protected Converter getConverter() {
        DataProvider provider = getProvider();
        
        return provider.getConverter();
    }
    
    protected Collection getPossibleObjects() {
        Object object = getInputContext().getParentContext().getMetaObject().getObject();

        if (getProviderClass() != null) {
            try {
                DataProvider provider = getProvider();
                return (Collection) provider.provide(object);
            }
            catch (Exception e) {
                throw new RuntimeException("exception while executing data provider", e);
            }
        }
        else {
            throw new RuntimeException("a data provider must be supplied");
        }
    }
    
    class MenuOptionLayout extends Layout {
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlMenu menu = new HtmlMenu();
            
            String defaultOptionTitle = RenderUtils.getResourceString("renderers.menu.default.title");
            menu.createDefaultOption(defaultOptionTitle).setSelected(object == null);
            
            RenderKit kit = RenderKit.getInstance();
            Schema schema = kit.findSchema(getEachSchema()); 
            
            for (Object obj : getPossibleObjects()) {
                MetaObject metaObject = MetaObjectFactory.createObject(obj, schema);
                MetaObjectKey key = metaObject.getKey();
                
                HtmlMenuOption option = menu.createOption(null);
                option.setValue(key.toString());
                
                if (getEachLayout() == null) {
                    option.setText(getObjectLabel(obj));
                }
                else {
                    PresentationContext newContext = getContext().createSubContext(metaObject);
                    newContext.setLayout(getEachLayout());
                    newContext.setRenderMode(RenderMode.getMode("output"));
                    
                    HtmlComponent component = kit.render(newContext, obj);
                    
                    option.setBody(component);
                }

                // select the option
                if (obj.equals(object)) {
                    option.setSelected(true);
                }
            }
            
            Converter converter = getConverter();
            if (converter != null) {
                menu.setConverter(new OptionConverter(converter));
            }
            
            menu.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            return menu;
        }

        protected String getObjectLabel(Object object) {
            if (getFormat() != null) {
                return RenderUtils.getFormatedProperties(getFormat(), object);
            }
            else {
                return String.valueOf(object);
            }
        }

    }
    
    private static class OptionConverter extends Converter {

        private Converter converter;

        public OptionConverter(Converter converter) {
            this.converter = converter;
        }

        @Override
        public Object convert(Class type, Object value) {
            String textValue = (String) value;
            
            if (textValue == null || textValue.length() == 0) {
                return null;
            }
            else {
                return this.converter.convert(type, value);
            }
        }
        
    }
}
