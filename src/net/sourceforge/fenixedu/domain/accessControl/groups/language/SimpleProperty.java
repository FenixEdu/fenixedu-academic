package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.PropertyNotAvailabeException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * A simple property is a property the applies directly to the value. The
 * property name is used as a java Bean property is obtained from the target
 * object.
 * 
 * @author cfgi
 */
public class SimpleProperty extends NestedProperty {

    private static final long serialVersionUID = 1L;

    public SimpleProperty(String name) {
        super(name);
    }

    /**
     * @inheritDoc
     * 
     * Obtains the value from the target by using the name as a simple java bean
     * property.
     * 
     * @exception PropertyNotAvailabeException
     *                when it wasn't possible to obtain the value from the
     *                target
     */
    @Override
    public Object getValue(Object target) {
        try {
            return PropertyUtils.getProperty(target, getName());
        } catch (Exception e) {
            throw new PropertyNotAvailabeException(e, target, getName());
        }
    }

}
