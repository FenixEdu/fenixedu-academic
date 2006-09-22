package net.sourceforge.fenixedu.domain.vigilancy;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class VigilantBound extends VigilantBound_Base {
    
    public  VigilantBound() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

	public VigilantBound(Vigilant vigilant, VigilantGroup group) {
		this();
		this.setVigilant(vigilant);
		this.setVigilantGroup(group);
		this.setConvokable(true);
	}
	
	public void delete() {
		removeVigilant();
		removeVigilantGroup();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

    
}
