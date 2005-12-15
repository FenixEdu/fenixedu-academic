package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.IProfessorship;

public class TeacherMasterDegreeService extends TeacherMasterDegreeService_Base {
    
    public TeacherMasterDegreeService(ITeacherService teacherService, IProfessorship professorship){
        super();
        setTeacherService(teacherService);
        setProfessorship(professorship);
    }
}
