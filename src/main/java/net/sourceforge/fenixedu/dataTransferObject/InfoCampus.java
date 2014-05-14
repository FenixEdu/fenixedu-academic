package net.sourceforge.fenixedu.dataTransferObject;

import org.fenixedu.spaces.domain.Space;

/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class InfoCampus extends InfoObject {
    private String name;

    public InfoCampus() {
    }

    /**
     * @param integer
     */
    public InfoCampus(String campusId) {
        super(campusId);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoCampus) {
            InfoCampus infoCampus = (InfoCampus) obj;
            result = getName().equals(infoCampus.getName());
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getExternalId();
        result += " name= " + getName();
        result += "]";
        return result;
    }

    public void copyFromDomain(Space campus) {
        super.copyFromDomain(campus);
        if (campus != null) {
            setName(campus.getName());
        }
    }

    public static InfoCampus newInfoFromDomain(Space campus) {
        InfoCampus infoCampus = null;
        if (campus != null) {
            infoCampus = new InfoCampus();
            infoCampus.copyFromDomain(campus);
        }
        return infoCampus;
    }
}