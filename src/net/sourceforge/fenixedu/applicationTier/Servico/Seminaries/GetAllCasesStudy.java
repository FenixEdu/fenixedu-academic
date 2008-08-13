/*
 * Created on 3/Set/2003, 14:22:08
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 14:22:08
 * 
 */
public class GetAllCasesStudy extends Service {

	public List run() throws BDException{
		List infoCases = new LinkedList();

		List cases = CaseStudy.getAllCaseStudies();

		for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
			CaseStudy caseStudy = (CaseStudy) iterator.next();

			infoCases.add(InfoCaseStudy.newInfoFromDomain(caseStudy));
		}

		return infoCases;
	}

}