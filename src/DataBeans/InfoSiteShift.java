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
    
    protected String orderByWeekDay;
    
    protected String orderByBeginHour;

    protected String orderByEndHour;
    
    protected String orderByRoom;
    
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
        
    public String getOrderByWeekDay() {
        return orderByWeekDay;
    }

    public void setOrderByWeekDay(String orderByWeekDay) {
    	this.orderByWeekDay = orderByWeekDay;
    }
    
    public String getOrderByBeginHour() {
        return orderByBeginHour;
    }

    public void setOrderByBeginHour(String orderByBeginHour) {
    	this.orderByBeginHour = orderByBeginHour;
    }

    public String getOrderByEndHour() {
        return orderByEndHour;
    }

    public void setOrderByEndHour(String orderByEndHour) {
    	this.orderByEndHour = orderByEndHour;
    }
    
    public String getOrderByRoom() {
        return orderByRoom;
    }

    public void setOrderByRoom(String orderByRoom) {
    	this.orderByRoom = orderByRoom;
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