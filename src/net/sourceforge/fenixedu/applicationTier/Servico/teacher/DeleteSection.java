package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Section;

public class DeleteSection extends Service {

    public Boolean run(final ExecutionCourse executionCourse, final Section section) {
        if (section != null) {
            section.delete();
        }
        return Boolean.TRUE;
    }

}