package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends ScientificArea_Base {

    public String toString() {
        return "name[" + getName() + "]";
    }

    public boolean equals(Object obj) {
        if (obj instanceof IScientificArea) {
            final IScientificArea scientificArea = (IScientificArea) obj;
            return this.getIdInternal().equals(scientificArea.getIdInternal());
        }
        return false;
    }

}
