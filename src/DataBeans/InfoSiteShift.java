/*
 * InfoShift.java
 *
 * Created on 31 de Outubro de 2002, 12:35
 */

package DataBeans;

/**
 * 
 * @author tfc130
 */

public class InfoSiteShift extends InfoObject {

    protected Object nrOfGroups;

    protected InfoShift infoShift;

    public Object getNrOfGroups() {
        return nrOfGroups;
    }

    public InfoShift getInfoShift() {
        return infoShift;
    }

    public void setNrOfGroups(Object nrOfGroups) {
        this.nrOfGroups = nrOfGroups;
    }

    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoSiteShift) {
            InfoSiteShift infoSiteShift = (InfoSiteShift) obj;
            resultado = (this.getNrOfGroups().equals(infoSiteShift.getNrOfGroups()))
                    && (this.getInfoShift().equals(infoSiteShift.getInfoShift()));
        }
        return resultado;
    }

}