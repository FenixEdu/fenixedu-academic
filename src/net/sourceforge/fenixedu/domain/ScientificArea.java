/*
 * AreaCientifica.java
 *
 * Created on 17 de Dezembro de 2003, 18:08
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends ScientificArea_Base {

    private List areaCurricularCourseGroups;

    public ScientificArea() {
    }

    /**
     * @return Returns the curricularCourseGroups.
     */
    public List getAreaCurricularCourseGroups() {
        return areaCurricularCourseGroups;
    }

    /**
     * @param curricularCourseGroups
     *            The curricularCourseGroups to set.
     */
    public void setAreaCurricularCourseGroups(List curricularCourseGroups) {
        this.areaCurricularCourseGroups = curricularCourseGroups;
    }

    public String toString() {
        return "name[" + getName() + "]";
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IScientificArea) {
            IScientificArea scientificArea = (IScientificArea) obj;
            result = scientificArea.getName().equals(getName());
        }
        return result;
    }
}