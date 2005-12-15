package relations;

public class TeacherServiceTeacherServiceItem extends TeacherServiceTeacherServiceItem_Base {
 
    public static void remove(net.sourceforge.fenixedu.domain.teacher.ITeacherServiceItem serviceItems, net.sourceforge.fenixedu.domain.teacher.ITeacherService teacherService) {
        TeacherServiceTeacherServiceItem_Base.remove(serviceItems,teacherService);
        if(teacherService.getServiceItems().isEmpty()){
            teacherService.delete();
        }
    }
}
