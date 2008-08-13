/*
 * Created on 4/Ago/2003, 13:05:42
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoTheme;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Ago/2003, 13:05:42
 * 
 */
public class GetEquivalency extends Service {

	public InfoEquivalency run(Integer equivalencyID) throws BDException{
		InfoEquivalency infoEquivalency = null;

		CourseEquivalency equivalency = rootDomainObject.readCourseEquivalencyByOID(equivalencyID);
		if (equivalency != null) {
			infoEquivalency = InfoEquivalency.newInfoFromDomain(equivalency);
			infoEquivalency.setThemes((List) CollectionUtils.collect(equivalency.getThemes(),
					new Transformer() {

						public Object transform(Object arg0) {

							return InfoTheme.newInfoFromDomain((Theme) arg0);
						}
					}));
		}

		return infoEquivalency;
	}
}