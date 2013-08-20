/*
 * Created on 4/Ago/2003, 13:05:42
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Ago/2003, 13:05:42
 * 
 */
public class GetEquivalency {

    protected InfoEquivalency run(String equivalencyID) {
        InfoEquivalency infoEquivalency = null;

        CourseEquivalency equivalency = AbstractDomainObject.fromExternalId(equivalencyID);
        if (equivalency != null) {
            infoEquivalency = InfoEquivalency.newInfoFromDomain(equivalency);
            infoEquivalency.setThemes((List) CollectionUtils.collect(equivalency.getThemes(), new Transformer() {

                @Override
                public Object transform(Object arg0) {

                    return InfoTheme.newInfoFromDomain((Theme) arg0);
                }
            }));
        }

        return infoEquivalency;
    }

    // Service Invokers migrated from Berserk

    private static final GetEquivalency serviceInstance = new GetEquivalency();

    @Service
    public static InfoEquivalency runGetEquivalency(String equivalencyID) throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(equivalencyID);
    }

}