/*
 * Created on 5/Ago/2003, 19:44:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 5/Ago/2003, 19:44:39
 * 
 */
public class GetCandidaciesByStudentIDAndSeminaryID {

    protected List run(String studentID, String seminaryID) {
        List candidaciesInfo = new LinkedList();

        Registration registration = FenixFramework.getDomainObject(studentID);
        Seminary seminary = FenixFramework.getDomainObject(seminaryID);

        List<SeminaryCandidacy> candidacies = SeminaryCandidacy.getByStudentAndSeminary(registration, seminary);

        for (SeminaryCandidacy candidacy : candidacies) {
            InfoCandidacy infoCandidacy = InfoCandidacy.newInfoFromDomain(candidacy);
            infoCandidacy.setSeminaryName(seminary.getName());

            candidaciesInfo.add(infoCandidacy);
        }

        return candidaciesInfo;
    }

    // Service Invokers migrated from Berserk

    private static final GetCandidaciesByStudentIDAndSeminaryID serviceInstance = new GetCandidaciesByStudentIDAndSeminaryID();

    @Atomic
    public static List runGetCandidaciesByStudentIDAndSeminaryID(String studentID, String seminaryID)
            throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(studentID, seminaryID);
    }

}