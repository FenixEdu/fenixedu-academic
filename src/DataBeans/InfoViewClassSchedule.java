/*
 * InfoViewClassSchedule.java
 * 
 * Created on 14 de Julho de 2003, 16:45
 */

package DataBeans;

import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

public class InfoViewClassSchedule extends InfoObject {
    protected InfoClass infoClass;

    protected List classLessons;

    public InfoViewClassSchedule() {
    }

    public InfoViewClassSchedule(InfoClass infoClass, List classLessons) {
        setInfoClass(infoClass);
        setClassLessons(classLessons);
    }

    public List getClassLessons() {
        return classLessons;
    }

    public InfoClass getInfoClass() {
        return infoClass;
    }

    public void setClassLessons(List list) {
        classLessons = list;
    }

    public void setInfoClass(InfoClass class1) {
        infoClass = class1;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoViewClassSchedule) {
            InfoViewClassSchedule infoViewClassSchedule = (InfoViewClassSchedule) obj;
            resultado = getInfoClass().equals(infoViewClassSchedule.getInfoClass())
                    && getClassLessons().size() == infoViewClassSchedule.getClassLessons().size();
        }
        return resultado;
    }

}