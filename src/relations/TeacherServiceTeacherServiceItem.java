package relations;

public class TeacherServiceTeacherServiceItem extends TeacherServiceTeacherServiceItem_Base {
 
    public static void remove(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem serviceItems, net.sourceforge.fenixedu.domain.teacher.TeacherService teacherService) {
        TeacherServiceTeacherServiceItem_Base.remove(serviceItems,teacherService);
        if(teacherService.getServiceItems().isEmpty()){
            teacherService.delete();
        }
    }
}
