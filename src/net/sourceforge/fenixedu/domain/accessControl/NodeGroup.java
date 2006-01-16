package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.IteratorChain;

import net.sourceforge.fenixedu.domain.Person;

public abstract class NodeGroup extends Group {
    
    private List<Group> children;
    
    public NodeGroup(Group ... groups) {
        super();
        
        this.children = new ArrayList<Group>();
        for (int i = 0; i < groups.length; i++) {
            this.children.add(groups[i]);
        }
    }
    
    public List<Group> getChildren() {
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
