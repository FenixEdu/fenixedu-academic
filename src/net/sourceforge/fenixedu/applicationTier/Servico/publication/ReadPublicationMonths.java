/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.util.Mes;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadPublicationMonths implements IServico {
    private static ReadPublicationMonths service = new ReadPublicationMonths();

    /**
     *  
     */
    private ReadPublicationMonths() {

    }

    public static ReadPublicationMonths getService() {

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadPublicationMonths";
    }

    public List run(String user, int publicationTypeId) {
        List MonthList = new ArrayList();
        int i = PublicationConstants.MONTHS_INIT;
        while (i < PublicationConstants.MONTHS_LIMIT) {
            Mes mes = new Mes(i);
            MonthList.add(mes.toString());
            i++;
        }
        return MonthList;
    }
}