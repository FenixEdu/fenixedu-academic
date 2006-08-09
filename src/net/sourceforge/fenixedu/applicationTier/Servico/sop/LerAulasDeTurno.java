/*
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAulasDeTurno
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;

public class LerAulasDeTurno extends Service {

	public List run(ShiftKey shiftKey) {

		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(shiftKey
				.getInfoExecutionCourse().getIdInternal());
		final Shift shift = executionCourse.findShiftByName(shiftKey.getShiftName());

		final List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
		for (final Lesson lesson : shift.getAssociatedLessons()) {
			infoAulas.add(InfoLesson.newInfoFromDomain(lesson));
		}
		return infoAulas;
	}

}
