/*
 * Created on 1/Ago/2003, 21:14:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import DataBeans.InfoObject;
import Dominio.Seminaries.IModality;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 1/Ago/2003, 21:14:43
 *  
 */
public class InfoModality extends InfoObject {

    private String description;

    private String name;

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    public void copyFromDomain(IModality modality) {
        super.copyFromDomain(modality);
        if (modality != null) {
            setDescription(modality.getDescription());
            setName(modality.getName());
        }
    }

    public static InfoModality newInfoFromDomain(IModality modality) {
        InfoModality infoModality = null;

        if (modality != null) {
            infoModality = new InfoModality();
            infoModality.copyFromDomain(modality);
        }
        return infoModality;
    }

}