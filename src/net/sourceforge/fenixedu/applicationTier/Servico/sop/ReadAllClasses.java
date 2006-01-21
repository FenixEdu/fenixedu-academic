/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteClassesComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;

/**
 * @author João Mota
 * 
 * 30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 * 
 */
public class ReadAllClasses extends Service {

	public SiteView run(Integer keyExecutionPeriod) throws FenixServiceException, ExcepcaoPersistencia {
		List infoClasses = null;

		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

		ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
				ExecutionPeriod.class, keyExecutionPeriod);

		ITurmaPersistente persistentClass = persistentSupport.getITurmaPersistente();
		List classes = persistentClass.readByExecutionPeriod(executionPeriod.getIdInternal());

		infoClasses = new ArrayList();
		Iterator iter = classes.iterator();
		while (iter.hasNext()) {
			SchoolClass dClass = (SchoolClass) iter.next();
			InfoClass infoClass = InfoClass.newInfoFromDomain(dClass);
			infoClasses.add(infoClass);
		}

		ISiteComponent classesComponent = new InfoSiteClassesComponent(infoClasses);
		SiteView siteView = new SiteView(classesComponent);

		return siteView;
	}

}