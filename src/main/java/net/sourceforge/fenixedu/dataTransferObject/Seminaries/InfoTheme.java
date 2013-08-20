/*
 * Created on 1/Ago/2003, 21:13:13
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 1/Ago/2003, 21:13:13
 * 
 */
public class InfoTheme extends InfoObject {

    private String description;

    private String name;

    private String shortName;

    public InfoTheme() {

    }

    public InfoTheme(String externalId) {
        super(externalId);
    }

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

    /**
     * @return
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param string
     */
    public void setShortName(String string) {
        shortName = string;
    }

    @Override
    public String toString() {
        String retorno;
        retorno = "[InfoTheme:";
        retorno += "ID=" + this.getExternalId();
        retorno += "Name=" + this.getName();
        retorno += ",Description=" + this.getDescription();
        retorno += ",Short Name=" + this.getShortName() + "]";
        return retorno;
    }

    public void copyFromDomain(Theme theme) {
        super.copyFromDomain(theme);
        if (theme != null) {
            setDescription(theme.getDescription());
            setName(theme.getName());
            setShortName(theme.getShortName());
        }
    }

    public static InfoTheme newInfoFromDomain(Theme theme) {
        InfoTheme infoTheme = null;

        if (theme != null) {
            infoTheme = new InfoTheme();
            infoTheme.copyFromDomain(theme);
        }
        return infoTheme;
    }

}