/*
 * Created on 30/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;

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
	        	infoFrequenta = InfoFrequentaWithAll.newInfoFromDomain ((IAttends)iterAttends.next());
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