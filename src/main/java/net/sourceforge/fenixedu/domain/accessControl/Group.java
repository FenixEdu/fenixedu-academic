/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * tags
 * Created on 23:13:44,20/Set/2005
 * @version $Id$
 */

package net.sourceforge.fenixedu.domain.accessControl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ArgumentList;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilderRegistry;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.set.UnmodifiableSet;
import org.fenixedu.bennu.core.domain.User;

/**
 * A <code>Group</code> is a dynamic aggregation of persons. It works as a
 * predicate that selects a subset of all the persons in the system.
 * <p>
 * Groups organization reflects the <em>composite</em> pattern. {@link net.sourceforge.fenixedu.domain.accessControl.LeafGroup
 * LeafGroup} represent concrete groups that select a collection of persons and
 * {@link net.sourceforge.fenixedu.domain.accessControl.NodeGroup NodeGroup} abstracts the composition of several groups, that is,
 * implement the intersection or union of groups.
 * <p>
 * Groups are considered a value type. Because of this no group should provide a mutator (or setter). All the required information
 * is passed to the group in the construction. New groups are formed though the composition of existing groups in a
 * {@link net.sourceforge.fenixedu.domain.accessControl.NodeGroup
 * NodeGroup}.
 * <p>
 * Groups are {@link java.io.Serializable Serializable} to allow an easy conversion to it's persisted format. Each sub group must
 * ensure that any changes mantain the compatibility with the persisted format or that the persisted groups are migrated
 * correctly.
 * 
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public abstract class Group implements Serializable, IGroup {

    private static final long serialVersionUID = 1L;

    private final Date creationDate;

    protected Group() {
        super();
        this.creationDate = new Date();
    }

    public Group with(final NodeGroup nodeGroup, final Group group) {
        return nodeGroup.with(this, group);
    }

    public Group without(final Group group) {
        Group result = this;

        if (this.equals(group)) {
            result = new EveryoneGroup();
        }

        return result;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public abstract java.util.Set<Person> getElements();

    /**
     * Provides a standard implementation to <code>count()</code><br/>
     * It accesses the elements set and invokes the <code>size()</code> method.
     * If any group subclassing this class can provide a more efficient way of
     * calculating its size, then override this method
     */
    @Override
    public int getElementsCount() {
        return this.getElements().size();
    }

    /**
     * Provides a standard implementation to <code>isMember()</code><br/>
     * It accesses the elements set and invokes the <code>contains()</code> method. If any group subclassing this class can
     * provide a more efficient
     * way of calculating wether the person is member of the group, then
     * override this method
     */
    @Override
    public boolean isMember(Person person) {
        return (person == null) ? false : getElements().contains(person);
    }

    @Override
    public boolean allows(User userView) {
        return isMember(userView == null ? null : userView.getPerson());
    }

    protected Set<Person> freezeSet(Set<Person> elements) {
        return UnmodifiableSet.decorate(elements);
    }

    protected Set<Person> buildSet() {
        // todo externalize this
        return new HashSet<Person>();
    }

    /**
     * Generates an group expression in the group expression language that
     * represents this group. If the group no longer can be represented as an
     * expression then this method returns <code>null</code>. This may happen
     * when a group depend on elements that are no longer available.
     * 
     * @return the string representation of this groups in a form understandable
     *         by the groups language parse or <code>null</code> when this group
     *         instance can no longer be represented as an expression
     * 
     *         TODO: move this default implementation to LeafGroup
     */
    @Override
    public String getExpression() {
        return getGroupExpressionName() + getExpressionArgumentsList();
    }

    public String getExpressionInHex() {
        String expression = getExpression();
        char[] encodeHex = Hex.encodeHex(expression.getBytes());
        return new String(encodeHex);
    }

    public static Group fromStringinHex(String string) {
        try {
            byte[] decodeHex = Hex.decodeHex(string.toCharArray());
            return fromString(new String(decodeHex));
        } catch (DecoderException e) {
            throw new Error(e);
        }
    }

    protected String getGroupExpressionName() {
        return getGroupExpressionName(this.getClass());
    }

    static public String getGroupExpressionName(final Class<? extends Group> groupClass) {
        String name = GroupBuilderRegistry.getNameOfBuilder(groupClass);

        if (name == null) {
            throw new DomainException("accessControl.group.expression.noName");
        }

        return name;
    }

    protected ArgumentList getExpressionArgumentsList() {
        ArgumentList argumentList = new ArgumentList();

        Argument[] expressionArguments = getExpressionArguments();
        if (expressionArguments != null) {
            for (Argument argument : expressionArguments) {
                argumentList.add(argument);
            }
        }

        return argumentList;
    }

    @Override
    public String getName() {
        final String name =
                BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), getPresentationNameKey(),
                        getPresentationNameKeyArgs());
        return name != null ? name : getExpression();
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return false;
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.GroupNameResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.name." + getClass().getSimpleName();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return null;
    }

    /**
     * @return the arguments required to define this group in the group
     *         expression
     */
    protected abstract Argument[] getExpressionArguments();

    public static Group fromString(String expr) {
        return ((expr == null) || expr.length() == 0) ? null : (new ExpressionGroup(expr).getGroup());
    }

    public static class InvalidGroupException extends RuntimeException {
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        throw new UnsupportedOperationException(this.getClass().getName() + " expr: " + getExpression()
                + " has no converter implemented");
    }
}
