package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumArrayConverter;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

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

    // HACK: duplicated code, id=inputChoices.selectPossibilitiesAndConverter
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
                    return new EnumArrayConverter(choiceTypeClass);
                }
                else {
                    return null;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("could not retrieve class named '" + getChoiceType() + "'");
            }
        }
    }

    // HACK: duplicated code, id=inputChoices.selectPossibilitiesAndConverter
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
                    
                    return RenderUtils.sortCollectionWithCriteria(result, getSortBy());
                }
                else {
                    return RenderUtils.sortCollectionWithCriteria(allChoices, getSortBy());
                }
            }
            catch (Exception e) {
                throw new RuntimeException("could not filter choices", e);
            }
        }
    }
    
    // HACK: duplicated code, id=inputChoices.selectPossibilitiesAndConverter
    private Collection readAllChoicesByType(String choiceType) {
        try {
            Class type = Class.forName(choiceType);

            if (DomainObject.class.isAssignableFrom(type)) {
                try {
                    return RootDomainObject.getInstance().readAllDomainObjects(type);
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
