package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.MenuOptionListRenderer;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * Fenix extension to the
 * {@link pt.ist.fenixWebFramework.renderers.MenuOptionListRenderer}.
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
     * This property is really an abbreviation for a data provider that read all
     * objects of a given type from the database. As such the class name given
     * must be the name of a subclass of
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
     * This property allows you to indicate a {@linkplain DataFilter data
     * filter} that will remove values, from the collection returned by data
     * provider, not valid in a specific context.
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
		    return new EnumConverter();
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
	    PresentationContext parentContext = getInputContext().getParentContext();
	    Object object = parentContext == null ? getInputContext().getMetaObject().getObject() : parentContext.getMetaObject()
		    .getObject();

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
		throw new RuntimeException("cannot generate choices automatically for type '" + choiceType + "'");
	    }
	} catch (Exception e) {
	    throw new RuntimeException("could not find type '" + choiceType + "' to generate choices");
	}

    }
}
