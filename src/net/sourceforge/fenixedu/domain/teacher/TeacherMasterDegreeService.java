package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Professorship;

public class TeacherMasterDegreeService extends TeacherMasterDegreeService_Base {
    
    public TeacherMasterDegreeService(TeacherService teacherService, Professorship professorship){
        super();
        setTeacherService(teacherService);
        setProfessorship(professorship);
    }
}
