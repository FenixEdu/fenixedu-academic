/*
 * AreaCientifica.java
 *
 * Created on 17 de Dezembro de 2003, 18:08
 */

package Dominio;

import java.util.List;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends DomainObject implements IScientificArea {
    private String name;

    private List areaCurricularCourseGroups;

    public ScientificArea() {
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
        return "name[" + name + "]";
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