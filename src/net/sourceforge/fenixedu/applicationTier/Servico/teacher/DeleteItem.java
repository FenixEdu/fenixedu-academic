package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem extends Service {

    public Boolean run(final ExecutionCourse executionCourse, final Item item) {
	if (item != null) {
	    item.delete();
	}
        return Boolean.TRUE;
    }

}

