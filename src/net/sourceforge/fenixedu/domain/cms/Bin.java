package net.sourceforge.fenixedu.domain.cms;

public abstract class Bin extends Bin_Base {

    public Bin() {
        super();
    }

    @Override
    public void delete() {
        for (Content child : this.getChildren()) {
            this.removeChildren(child);
        }

        for (Bin parent : this.getParents()) {
            parent.removeChildren(this);
        }

        super.delete();
    }
}
