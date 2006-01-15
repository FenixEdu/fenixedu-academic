/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import org.apache.struts.util.MessageResources;

/**
 * @author Tânia Pousão
 * 
 */
public class PaymentPhase extends PaymentPhase_Base {

    public String toString() {
        StringBuilder object = new StringBuilder();
        object = object.append("\n[PaymentPhase: ").append("idInternal= ").append(getIdInternal())
                .append(" starDate= ").append(getStartDate()).append("; endDate= ").append(getEndDate())
                .append("; value= ").append(getValue()).append("; description= ").append(
                        getDescriptionFromMessageResourses()).append("\n");

        return object.toString();
    }

    public String getDescriptionFromMessageResourses() {
        MessageResources messages = MessageResources
            .getMessageResources("ServidorApresentacao.ApplicationResources");

        String newDescription = null;
        newDescription = messages.getMessage(super.getDescription());
        if (newDescription == null) {
            newDescription = super.getDescription();
        }
        return newDescription;
    }

}
