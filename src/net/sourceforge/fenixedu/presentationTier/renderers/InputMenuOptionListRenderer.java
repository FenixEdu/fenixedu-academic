package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.EnumInputRenderer;
import net.sourceforge.fenixedu.renderers.MenuOptionListRenderer;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * Fenix extension to the {@link net.sourceforge.fenixedu.renderers.MenuOptionListRenderer}.
 * 
 * {@inheritDoc}
 * 
 * @author cfgi
 */
public class InputMenuOptionListRenderer extends MenuOptionListRenderer {
    private String choiceType;
    
    private String filterClass;
    
    public String getChoiceType() {
        return this.choiceType;
    }

    /**
     * This property is really an abbreviation for a data provider that
     * read all objects of a given type from the database. As such the
     * class name given must be the name of a subclass of 
     * {@link net.sourceforge.fenixedu.domain.DomainObject}.
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
     * This property allows you to indicate a {@linkplain DataFilter data filter}
     * that will remove values, from the collection returned by data provider,
     * not valid in a specific context.
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
            try {
                Class choiceTypeClass = Class.forName(getChoiceType());
                
                if (DomainObject.class.isAssignableFrom(choiceTypeClass)) {
                    return new DomainObjectKeyConverter(); 
                }
                else if (Enum.class.isAssignableFrom(choiceTypeClass)) {
                    return new EnumInputRenderer.EnumConverter();
                }
                else {
                    return null;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("could not retrieve class named '" + getChoiceType() + "'");
            }
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
            Class type = Class.forName(choiceType);

            if (DomainObject.class.isAssignableFrom(type)) {
                try {
                    IUserView userView = AccessControl.getUserView();

                    return (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects",
                            new Object[] { type });
                } catch (Exception e) {
                    throw new RuntimeException("could not read all objects of type " + choiceType);
                }
            } else if (Enum.class.isAssignableFrom(type)) {
                List result = new ArrayList();
                Object[] constants = type.getEnumConstants();

                for (int i = 0; i < constants.length; i++) {
                    result.add(constants[i]);
                }
                
                return result;
            } else {
                throw new RuntimeException("cannot generate choices automatically for type '" + choiceType + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("could not find type '" + choiceType + "' to generate choices");
        }
        
    }
}
