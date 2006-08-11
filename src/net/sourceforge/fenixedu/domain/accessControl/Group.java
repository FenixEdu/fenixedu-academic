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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;

/**
 * A <code>Group</code> is a dynamic aggregation of persons. It works as a
 * predicate that selects a subset of all the persons in the system.
 * <p>
 * Groups organization reflects the <em>composite</em> pattern.
 * {@link net.sourceforge.fenixedu.domain.accessControl.LeafGroup LeafGroup}
 * represent concrete groups that select a collection of persons and
 * {@link net.sourceforge.fenixedu.domain.accessControl.NodeGroup NodeGroup}
 * abstracts the composition of several groups, that is, implement the
 * intersection or union of groups.
 * <p>
 * Groups are considered a value type. Because of this no group should provide a
 * mutator (or setter). All the required information is passed to the group in
 * the construction. New groups are formed though the composition of existing
 * groups in a
 * {@link net.sourceforge.fenixedu.domain.accessControl.NodeGroup NodeGroup}.
 * <p>
 * Groups are {@link java.io.Serializable Serializable} to allow an easy
 * conversion to it's persisted format. Each sub group must ensure that any
 * changes mantain the compatibility with the persisted format or that the
 * persisted groups are migrated correctly.
 */
public abstract class Group implements Serializable, IGroup {

    private static final long serialVersionUID = 1L;

    private Date creationDate;

    protected Group() {
        super();

        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public abstract java.util.Set<Person> getElements();

    /**
     * Provides a standard implementation to <code>count()</code><br/> It
     * accesses the elements set and invokes the <code>size()</code> method.
     * If any group subclassing this class can provide a more efficient way of
     * calculating its size, then override this method
     */
    public int getElementsCount() {
        return this.getElements().size();
    }

    /**
     * Provides a standard implementation to <code>isMember()</code><br/> It
     * accesses the elements set and invokes the <code>contains()</code>
     * method. If any group subclassing this class can provide a more efficient
     * way of calculating wether the person is member of the group, then
     * override this method
     */
    public boolean isMember(Person person) {
        return this.getElements().contains(person);
    }

    public boolean allows(IUserView userView) {
        return this.isMember(userView.getPerson());
    }

    protected Set<Person> freezeSet(Set<Person> elements) {
        return Collections.unmodifiableSet(elements);
    }

    protected Set<Person> buildSet() {
        // todo externalize this
        return new HashSet<Person>();
    }
}
