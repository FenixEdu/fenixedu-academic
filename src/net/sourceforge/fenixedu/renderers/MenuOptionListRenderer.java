package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class MenuOptionListRenderer extends InputRenderer {
    private String format; 
    
    private String providerClass;

    private DataProvider provider;
    
    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getProviderClass() {
        return this.providerClass;
    }

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
            
            for (Object obj : getPossibleObjects()) {
                String text = getObjectLabel(obj);
                
                MetaObject metaObject = MetaObjectFactory.createObject(obj, null);
                MetaObjectKey key = metaObject.getKey();
                
                HtmlMenuOption option = menu.createOption(text);
                option.setValue(key.toString());
            }
            
            DataProvider provider = getProvider();
            if (provider.getConverter() != null) {
                menu.setConverter(provider.getConverter());
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
}
