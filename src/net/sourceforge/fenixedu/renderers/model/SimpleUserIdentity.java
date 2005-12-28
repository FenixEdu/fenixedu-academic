package net.sourceforge.fenixedu.renderers.model;

public class SimpleUserIdentity implements UserIdentity {

    private String id;

    public SimpleUserIdentity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof SimpleUserIdentity)) {
            return false;
        }
        
        SimpleUserIdentity otherIdentity = (SimpleUserIdentity) other;
        
        if (this.id == null && otherIdentity.id != null) {
            return false;
        }
        
        if (this.id != null) {
            return this.id.equals(otherIdentity.id);
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        return this.id == null ? 0 : this.id.hashCode();
    }
}
