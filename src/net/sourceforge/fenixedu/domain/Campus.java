package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class Campus extends Campus_Base {

	public boolean equals(final Object obj) {
        if (obj instanceof ICampus) {
            final ICampus campus = (ICampus) obj;
            return getName().equals(campus.getName());
        }
        return false;
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