package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.ArrayList;

public class ArgumentList extends ArrayList<Argument> {

    private static final long serialVersionUID = 1L;

    /**
     * Transforms the list of {@link Argument} into the final argument values.
     * 
     * @return an array of argument values that correspond to the list of
     *         arguments
     */
    public Object[] getArgumentValues() {
        Object[] result = new Object[size()];

        int index = 0;
        for (Argument argument : this) {
            result[index] = argument.getValue();

            index++;
        }

        return result;
    }

    /**
     * Transforms the list of {@link Argument} into an array containing the
     * types all the arguments.
     * 
     * @return an array with the types of every argument
     */
    public Class[] getArgumentTypes() {
        Class[] result = new Class[size()];
        
        int index = 0;
        for (Object value : getArgumentValues()) {
            result[index] = value.getClass();
            
            index++;
        }
        
        return result;
    }
}
