package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBoxList;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class InputCheckBoxListRenderer extends InputRenderer {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private String providerClass;
    
    private String choiceType;
    
    private String filterClass;
    
    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    public void setEachStyle(String style) {
        this.eachStyle = style;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getProviderClass() {
        return this.providerClass;
    }

    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    public String getChoiceType() {
        return this.choiceType;
    }

    public void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    public String getFilterClass() {
        return this.filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new CheckBoxListLayout();
    }

    protected Collection getPossibleObjects() {
        Object object = getInputContext().getParentContext().getMetaObject().getObject();

        if (getProviderClass() != null) {
            String className = getProviderClass();
    
            try {
                Class providerCass = (Class<DataProvider>) Class.forName(className);
                DataProvider provider = (DataProvider) providerCass.newInstance();  
                
                return (Collection) provider.provide(object);
            }
            catch (Exception e) {
                throw new RuntimeException("exception while executing data provider", e);
            }
        }
        else {
            String choiceType = getChoiceType();
            String filterClassName = getFilterClass();
            
            try {
                Collection allChoices = readAllChoicesByType(choiceType);

                if (getFilterClass() != null) {
                    Class filterClass = Class.forName(filterClassName);
                    DataFilter filter = (DataFilter) filterClass.newInstance();
    
                    List result = new ArrayList();
                    for (Object choice : allChoices) {
                        if (filter.acccepts(object, choice)) {
                            result.add(object);
                        }
                    }
                    
                    return result;
                }
                else {
                    return allChoices;
                }
            }
            catch (Exception e) {
                throw new RuntimeException("could not filter choices", e);
            }
        }
    }
    
    private Collection readAllChoicesByType(String choiceType) {
        try {
            IUserView userView = AccessControl.getUserView();
            Class type = Class.forName(choiceType);
            
            return (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { type });
        } catch (Exception e) {
            throw new RuntimeException("could not read all objects of type " + choiceType);
        }
    }

    class CheckBoxListLayout extends Layout {
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Collection collection = (Collection) object;
            
            HtmlCheckBoxList listComponent = new HtmlCheckBoxList();
            
            for (Object obj : getPossibleObjects()) {
                Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
                String layout = getEachLayout();
                
                MetaObject metaObject = MetaObjectFactory.createObject(obj, schema);
                MetaObjectKey key = metaObject.getKey();
                
                PresentationContext newContext = getContext().createSubContext(metaObject);
                newContext.setLayout(layout);
                newContext.setRenderMode(RenderMode.getMode("output"));
                
                RenderKit kit = RenderKit.getInstance();
                HtmlComponent component = kit.render(newContext, obj);
                HtmlCheckBox checkBox = listComponent.addOption(component, key.toString());
                
                if (collection != null && collection.contains(obj)) {
                    checkBox.setChecked(true);
                }
            }
     
            listComponent.setConverter(new DomainObjectKeyArrayConverter());
            listComponent.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            return listComponent;
        }

    }
}
