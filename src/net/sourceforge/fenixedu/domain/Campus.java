package net.sourceforge.fenixedu.domain;

/**
 * @author T�nia Pous�o Create on 10/Nov/2003
 */
public class Campus extends Campus_Base {

	

    public Campus() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[INFODEGREE_INFO: idInternal= ");
		stringBuilder.append(getIdInternal());
		stringBuilder.append(" name= ");
		stringBuilder.append(getName());
		stringBuilder.append("]");
        return stringBuilder.toString();
    }
}