package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

/**
 * Custom group builder that can be used to create a group with explicitly register
 * a builder for it. This builder accepts the type of the group to create and
 * all the arguments to pass and tries to create the group from those arguments.
 * 
 * <p>
 * Example:
 * <code>custom($C('accessControl.RoleGroup'), 'MANAGER')</code>
 * 
 * @author cfgi
 * 
 * @see net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.ClassOperator
 */
class CustomGroupBuilder implements GroupBuilder {

    public Group build(Object[] arguments) {
        if (arguments.length < 1) {
            throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(), getMaxArguments());
        }
        
        try {
            Class groupClass = (Class) arguments[0];

            if (! Group.class.isAssignableFrom(groupClass)) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.custom.class.notGroup", groupClass.getName());
            }
            
            Class[] types = new Class[arguments.length - 1];
            Object[] values = new Object[arguments.length - 1];
            
            for (int i = 1; i < arguments.length; i++) {
                values[i - 1] = arguments[i];
                types[i - 1] = arguments[i].getClass();
            }
            
            Constructor constructor = groupClass.getConstructor(types);
            return (Group) constructor.newInstance(values);
        }
        catch (ClassCastException e) {
            throw new WrongTypeOfArgumentException(1, Class.class, arguments[0].getClass());
        } catch (NoSuchMethodException e) {
            throw new GroupDynamicExpressionException(e, "accessControl.group.builder.custom.constructor.doesNotExist", Arrays.asList(arguments).toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new GroupDynamicExpressionException(e, "accessControl.group.builder.custom.constructor.exception", Arrays.asList(arguments).toString());
        } catch (GroupDynamicExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new GroupDynamicExpressionException(e, "accessControl.group.builder.custom.constructor.failed", Arrays.asList(arguments).toString());
        }
    }

    public int getMinArguments() {
        return 1;
    }

    public int getMaxArguments() {
        return Integer.MAX_VALUE;
    }
    
}