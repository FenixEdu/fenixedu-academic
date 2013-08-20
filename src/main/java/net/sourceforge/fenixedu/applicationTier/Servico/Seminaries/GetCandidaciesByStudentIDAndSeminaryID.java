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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

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

        Registration registration = AbstractDomainObject.fromExternalId(studentID);
        Seminary seminary = AbstractDomainObject.fromExternalId(seminaryID);

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

    @Service
    public static List runGetCandidaciesByStudentIDAndSeminaryID(String studentID, String seminaryID)
            throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(studentID, seminaryID);
    }

}