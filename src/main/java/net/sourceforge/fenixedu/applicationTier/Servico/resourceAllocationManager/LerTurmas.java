package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerTurmas {

    @Atomic
    public static List<InfoClass> run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());

        final Set<SchoolClass> classes;
        if (curricularYear != null) {
            classes = executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionSemester, curricularYear);
        } else {
            classes = executionDegree.findSchoolClassesByExecutionPeriod(executionSemester);
        }

        final List<InfoClass> infoClassesList = new ArrayList<InfoClass>();

        for (final SchoolClass schoolClass : classes) {
            InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClassesList.add(infoClass);
        }

        return infoClassesList;
    }

}