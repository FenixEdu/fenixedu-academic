/*
 * Created on 30/Ago/2004
 */
package DataBeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import Dominio.IAttendsSet;
import Dominio.IFrequenta;

/**
 * @author joaosa & rmalo
 */
public class InfoAttendsSetWithInfoAttendsWithInfoGroupProperties extends InfoAttendsSetWithInfoGroupProperties{



	public void copyFromDomain(IAttendsSet attendsSet) {		
		super.copyFromDomain(attendsSet);
		
		 if (attendsSet != null) {
		 List listaInfoAttend = new ArrayList();
	        Iterator iterAttends = attendsSet.getAttends().iterator();
	        InfoFrequenta infoFrequenta;
	        while(iterAttends.hasNext()){
	        	infoFrequenta = InfoFrequentaWithAll.newInfoFromDomain ((IFrequenta)iterAttends.next());
	        	listaInfoAttend.add(infoFrequenta);
	        }
	        setInfoAttends(listaInfoAttend);	
		 }
	}

	public static InfoAttendsSet newInfoFromDomain(
            IAttendsSet attendsSet) {
		InfoAttendsSetWithInfoAttendsWithInfoGroupProperties infoAttendsSet = null;
        if (attendsSet != null) {
            infoAttendsSet = new InfoAttendsSetWithInfoAttendsWithInfoGroupProperties();
            infoAttendsSet.copyFromDomain(attendsSet);
        }

        return infoAttendsSet;
    }
}