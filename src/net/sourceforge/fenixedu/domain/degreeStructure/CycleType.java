package net.sourceforge.fenixedu.domain.degreeStructure;


public enum CycleType {

    FIRST_CYCLE,

    SECOND_CYCLE,

    THIRD_CYCLE;

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

}
