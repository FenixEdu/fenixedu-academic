package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 * 
 *  
 */
public class ResponsibleFor extends ResponsibleFor_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IResponsibleFor) {
            final IResponsibleFor responsibleFor = (IResponsibleFor) obj;
            return this.getIdInternal().equals(responsibleFor.getIdInternal());
        }
        return false;
    }

}
