package net.sourceforge.fenixedu.domain.curriculum;



public interface IGrade extends Comparable<IGrade>{
    
    public Object getGradeValue();

    public GradeType getGradeType();
}
