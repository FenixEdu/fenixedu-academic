package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This is the Fenix extension to the {@link net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer}.
 * 
 * {@inheritDoc}
 * 
 * @author cfgi
 */
public class InputCheckBoxListRenderer extends CheckBoxOptionListRenderer {
    private String choiceType;
    
    private String filterClass;
    
    public String getChoiceType() {
        return this.choiceType;
    }

    /**
     * This property is an abbreviation for a data provider that provides all objects
     * of a given type. The class named given must be a {@link net.sourceforge.fenixedu.domain.DomainObject}
     * beacuse this renderers tries to read all objects using the <tt>ReadAllDomainObjects</tt> service.
     * 
     * @property
     */
    public void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    public String getFilterClass() {
        return this.filterClass;
    }

    /**
     * Since all objects of a given type are selected with {@link #setChoiceType(String) choiceType}, this
     * property allows you to specify a {@link DataFilter data filter} that filters objects that are
     * not allowed from the collection created by the provider.
     * 
     * @property
     */
    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    @Override
    protected Converter getConverter() {
        if (getProviderClass() != null) {
            return super.getConverter();
        }
        else {
            return new DomainObjectKeyArrayConverter();
        }
    }

    @Override
    protected Collection getPossibleObjects() {
        if (getProviderClass() != null) {
            return super.getPossibleObjects();
        }
        else {
            Object object = getInputContext().getParentContext().getMetaObject().getObject();
            
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
}
