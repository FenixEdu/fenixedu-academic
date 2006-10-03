package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;

/**
 * The <tt>GroupBuilderBean</tt> associates a group builder with the name under which it's
 * registered.
 * 
 * @author cfgi
 */
public class GroupBuilderBean implements Serializable, Comparable<GroupBuilderBean> {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private GroupBuilder builder;
    
    public GroupBuilderBean(String name, GroupBuilder builder) {
        this.name = name;
        this.builder = builder;
    }

    public String getName() {
        return this.name;
    }

    public String getArguments() {
        int min = getBuilder().getMinArguments();
        int max = getBuilder().getMaxArguments();
        
        if (min != max) {
            return String.format("[%d; %s]", min, max == Integer.MAX_VALUE ? "inf" : String.valueOf(max));
        }
        else {
            return String.format("%d", min);
        }
    }
    
    public GroupBuilder getBuilder() {
        return this.builder;
    }

    public int compareTo(GroupBuilderBean bean) {
        if (bean == null) {
            return 1;
        }
        else {
            return getName().compareTo(bean.getName());
        }
    }
    
}
