package net.sourceforge.fenixedu.domain;

public class EmptyGrade extends Grade {
    
    protected EmptyGrade() {
    }
    
    @Override
    public String getValue() {
        return null;
    }
    
    @Override
    public GradeScale getGradeScale() {
        return null;
    }
    
    @Override
    public String exportAsString() {
        return null;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean isNumeric() {
        return false;
    }
    
}
