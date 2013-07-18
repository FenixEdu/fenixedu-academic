package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.services.Service;

public class LerTurmas {

    @Service
    public static List<InfoClass> run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) {

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        final ExecutionSemester executionSemester =
                RootDomainObject.getInstance().readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());

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