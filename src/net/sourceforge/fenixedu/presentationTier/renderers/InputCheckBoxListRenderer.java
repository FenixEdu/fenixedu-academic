package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumArrayConverter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This is the Fenix extension to the
 * {@link net.sourceforge.fenixedu.renderers.CheckBoxOptionListRenderer}.
 * 
 * {@inheritDoc}
 * 
 * @author cfgi
 */
public class InputCheckBoxListRenderer extends CheckBoxOptionListRenderer {
    private String choiceType;

    private String filterClass;

    private String emptyMessageKey;

    private String emptyMessageBundle;

    private String emptyMessageClasses;

    /**
     * This property allows you to configure the css classes used in the empty
     * message
     * 
     * @property
     */

    public String getEmptyMessageClasses() {
        return emptyMessageClasses;
    }

    public void setEmptyMessageClasses(String emptyMessageClasses) {
        this.emptyMessageClasses = emptyMessageClasses;
    }

    /**
     * This property allows you to configure the bundle that the empty message
     * key uses
     * 
     * @property
     */
    public String getEmptyMessageBundle() {
        return emptyMessageBundle;
    }

    public void setEmptyMessageBundle(String emptyMessageBundle) {
        this.emptyMessageBundle = emptyMessageBundle;
    }

    /**
     * This property allows you to configure a display message in case the
     * object list is empty
     * 
     * @property
     */
    public String getEmptyMessageKey() {
        return emptyMessageKey;
    }

    public void setEmptyMessageKey(String emptyMessageKey) {
        this.emptyMessageKey = emptyMessageKey;
    }

    public String getChoiceType() {
        return this.choiceType;
    }

    /**
     * This property is an abbreviation for a data provider that provides all
     * objects of a given type. The class named given must be a
     * {@link net.sourceforge.fenixedu.domain.DomainObject} beacuse this
     * renderers tries to read all objects using the
     * <tt>ReadAllDomainObjects</tt> service.
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
     * Since all objects of a given type are selected with
     * {@link #setChoiceType(String) choiceType}, this property allows you to
     * specify a {@link DataFilter data filter} that filters objects that are
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
        } else {
            try {
                Class choiceTypeClass = Class.forName(getChoiceType());

                if (DomainObject.class.isAssignableFrom(choiceTypeClass)) {
                    return new DomainObjectKeyConverter();
                } else if (Enum.class.isAssignableFrom(choiceTypeClass)) {
                    return new EnumArrayConverter(choiceTypeClass);
                } else {
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
        } else {
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
                } else {
                    return RenderUtils.sortCollectionWithCriteria(allChoices, getSortBy());
                }
            } catch (Exception e) {
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
                throw new RuntimeException("cannot generate choices automatically for type '"
                        + choiceType + "'");
            }
        } catch (Exception e) {
            throw new RuntimeException("could not find type '" + choiceType + "' to generate choices");
        }
    }

    @Override
    public Layout getLayout(Object object, Class type) {
        return new InputCheckBoxLayoutWithEmptyMessage();
    }

    class InputCheckBoxLayoutWithEmptyMessage extends CheckBoxListLayout {

        private boolean empty;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Collection collection = (Collection) object;
            HtmlComponent component;

            if (getEmptyMessageKey() != null && collection.isEmpty() && getPossibleObjects().isEmpty()) {
                component = new HtmlText(RenderUtils.getResourceString(getEmptyMessageBundle(),
                        getEmptyMessageKey()));
                this.empty = true;
            } else {
                component = super.createComponent(object, type);
                this.empty = false;
            }
            return component;
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            if (this.empty) {
                component.setClasses(getEmptyMessageClasses());
            } else {
                super.applyStyle(component);
            }

        }
    }
}