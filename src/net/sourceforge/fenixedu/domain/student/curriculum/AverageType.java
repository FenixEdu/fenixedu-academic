/**
 * 
 */
package net.sourceforge.fenixedu.domain.student.curriculum;

public enum AverageType {
    SIMPLE, WEIGHTED, BEST;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return getClass().getName() + "." + name();
    }

}
