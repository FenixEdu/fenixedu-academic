/*
 * Created on 18/Jun/2004
 *  
 */
package DataBeans;

import Dominio.IGroupProperties;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoGroupPropertiesWithInfoExecutionCourse extends
        InfoGroupProperties {

    public void copyFromDomain(IGroupProperties groupProperties) {
        super.copyFromDomain(groupProperties);
        if (groupProperties != null) {
            setInfoExecutionCourse(InfoExecutionCourse
                    .newInfoFromDomain(groupProperties.getExecutionCourse()));
        }
    }

    public static InfoGroupProperties newInfoFromDomain(
            IGroupProperties groupProperties) {
        InfoGroupPropertiesWithInfoExecutionCourse infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGroupPropertiesWithInfoExecutionCourse();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }
}