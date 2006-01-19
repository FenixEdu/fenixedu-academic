package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.IteratorChain;

import net.sourceforge.fenixedu.domain.Person;

public abstract class NodeGroup extends Group {
    
    private static final long serialVersionUID = 1L;
    
    private List<Group> children;
    
    protected NodeGroup() {
        super();
        
        this.children = new ArrayList<Group>();
    }
    
    public NodeGroup(Group ... groups) {
        this();
        
        for (Group group : groups) {
            this.children.add(group);
        }
    }
    
    public NodeGroup(Collection<Group> groups) {
        this();
        
        this.children.addAll(groups);
    }
    
    protected List<Group> getChildren() {
        return this.children;
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        IteratorChain chain = new IteratorChain();
        
        for (Iterator iterator = getChildren().iterator(); iterator.hasNext();) {
            Group group = (Group) iterator.next();
            
            chain.addIterator(group.getElementsIterator());
        }

        return chain;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        
        if (! this.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }

        NodeGroup otherNodeGroup = (NodeGroup) other;
        return this.children.equals(otherNodeGroup.children);
    }

    @Override
    public int hashCode() {
        return this.children.hashCode();
    }
}
