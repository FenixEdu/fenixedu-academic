/*
 * Created on 30/Jun/2003
 *
 * 
 */
package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoClass;
import DataBeans.InfoSiteClassesComponent;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.ISchoolClass;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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