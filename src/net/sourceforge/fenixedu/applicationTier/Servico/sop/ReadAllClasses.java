/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteClassesComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 * 
 * 30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 *  
 */
public class ReadAllClasses implements IServico {

    /**
     *  
     */
    private ReadAllClasses() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAllClasses";
    }

    private static ReadAllClasses service = new ReadAllClasses();

    public static ReadAllClasses getService() {
        return service;
    }

    public SiteView run(Integer keyExecutionPeriod) throws FenixServiceException {
        List infoClasses = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                    ExecutionPeriod.class, keyExecutionPeriod);

            ITurmaPersistente persistentClass = sp.getITurmaPersistente();
            List classes = persistentClass.readByExecutionPeriod(executionPeriod);

            infoClasses = new ArrayList();
            Iterator iter = classes.iterator();
            while (iter.hasNext()) {
                ISchoolClass dClass = (ISchoolClass) iter.next();
                InfoClass infoClass = Cloner.copyClass2InfoClass(dClass);
                infoClasses.add(infoClass);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        ISiteComponent classesComponent = new InfoSiteClassesComponent(infoClasses);
        SiteView siteView = new SiteView(classesComponent);

        return siteView;
    }

}