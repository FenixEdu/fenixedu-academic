package net.sourceforge.fenixedu.domain.teacher;

public abstract class TeacherServiceItem extends TeacherServiceItem_Base {
 
    public TeacherServiceItem() {
        super();
        this.setOjbConcreteClass(this.getClass().getName());
    }
    
}
