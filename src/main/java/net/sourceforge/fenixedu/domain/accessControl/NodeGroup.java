package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.injectionCode.IGroup;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public abstract class NodeGroup extends Group {

    private static final long serialVersionUID = 1L;

    private final List<IGroup> children;

    protected NodeGroup() {
        super();

        this.children = new ArrayList<IGroup>();
    }

    public NodeGroup(IGroup... groups) {
        this();

        for (IGroup group : groups) {
            this.children.add(group);
        }
    }

    public NodeGroup(Collection<IGroup> groups) {
        this();

        this.children.addAll(groups);
    }

    public List<IGroup> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!this.getClass().equals(other.getClass())) {
            return false;
        }

        NodeGroup otherNodeGroup = (NodeGroup) other;
        return this.children.equals(otherNodeGroup.children);
    }

    @Override
    public int hashCode() {
        return this.children.hashCode();
    }

    abstract public Group with(final Group... group);

    @Override
    public Group with(final NodeGroup nodeGroup, final Group group) {
        Group result = null;

        if (this.getClass().equals(nodeGroup.getClass())) {
            result = this.with(group);
        } else {
            result = super.with(nodeGroup, group);
        }

        return result;
    }

    @Override
    public String getExpression() {
        StringBuilder builder = new StringBuilder();

        Iterator<IGroup> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            builder.append(getChildrenExpression(iterator.next()));

            if (iterator.hasNext()) {
                builder.append(" " + getExpressionOperator() + " ");
            }
        }

        return builder.toString();
    }

    private String getChildrenExpression(IGroup group) {
        if (group instanceof NodeGroup) {
            return "(" + group.getExpression() + ")";
        } else {
            return group.getExpression();
        }
    }

    protected abstract String getExpressionOperator();

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

}
