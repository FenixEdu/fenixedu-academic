package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupBuilderNameTakenException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NoSuchGroupBuilderException;

import org.apache.log4j.Logger;

/**
 * Keeps all the registered {@link GroupBuilder} by name.
 * 
 * @author cfgi
 */
public class GroupBuilderRegistry {

    private static final Logger logger = Logger.getLogger(GroupBuilderRegistry.class);

    private static final GroupBuilderRegistry instance = new GroupBuilderRegistry();

    private Map<String, GroupBuilder> builders;

    private GroupBuilderRegistry() {
        this.builders = new Hashtable<String, GroupBuilder>();
    }

    /**
     * Registers a new group builder by name.
     * 
     * @param name
     *            the name of the builder
     * @param builder
     *            the builder implementation
     * 
     * @exception GroupBuilderNameTakenException
     *                if a group builder with the same name was already
     *                registered
     */
    public static void register(String name, GroupBuilder builder) {
        if (instance.builders.containsKey(name)) {
            throw new GroupBuilderNameTakenException(name);
        }

        logger.debug("registering builder: " + name + " = " + builder);
        instance.builders.put(name, builder);
    }

    /**
     * Removes the group builder with the given name.
     * 
     * @param name
     *            the name of the group builder to remove
     */
    public static void unregister(String name) {
        instance.builders.remove(name);
    }

    /**
     * Obtains a group builder by name.
     * 
     * @param name
     *            the name of the group builder
     * @return the group builder registered under the given name
     * 
     * @exception NoSuchGroupBuilderException
     *                when there is no group builder registered with the given
     *                name
     */
    public static GroupBuilder getGroupBuilder(String name) {
        if (!instance.builders.containsKey(name)) {
            throw new NoSuchGroupBuilderException(name);
        }

        return instance.builders.get(name);
    }

    //
    // register builders
    //
    
    static {
        register("role", new RoleGroup.Builder());
        register("custom", new CustomGroupBuilder());
    }
}
