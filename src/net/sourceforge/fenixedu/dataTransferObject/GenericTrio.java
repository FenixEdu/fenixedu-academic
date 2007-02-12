package net.sourceforge.fenixedu.dataTransferObject;


/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class GenericTrio<T, U, V> {

    private T first;

    private U second;

    private V third;

    public GenericTrio(T first, U second, V third) {
        super();
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public V getThird() {
        return third;
    }

    public void setThird(V third) {
        this.third = third;
    }

    public boolean equals(Object obj) {
	GenericTrio genericTrio = null;
	if (obj instanceof GenericTrio) {
	    genericTrio = (GenericTrio) obj;
	} else {
	    return false;
	}
	return (this.getFirst().equals(genericTrio.getFirst())
		&& this.getSecond().equals(genericTrio.getSecond()) 
		&& this.getThird().equals(genericTrio.getThird()));
    }

    public int hashCode() {
	final StringBuilder builder = new StringBuilder();
	builder.append(String.valueOf(getFirst().hashCode()));
	builder.append('@');
	builder.append(String.valueOf(getSecond().hashCode()));
	builder.append('@');
	builder.append(String.valueOf(getThird().hashCode()));
	return builder.toString().hashCode();
    }
}
