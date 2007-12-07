package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class DegreeSite extends DegreeSite_Base {

    public DegreeSite(Degree degree) {
        super();
        
        setDegree(degree);
    }
    
    @Override
    public IGroup getOwner() {
        return new CurrentDegreeCoordinatorsGroup(getDegree());
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
        
        groups.add(new CurrentDegreeCoordinatorsGroup(getDegree()));
        
        return groups;
    }

    @Override
    protected void deleteRelations() {
        super.deleteRelations();
        
        removeDegree();
    }

    
    
    @Override
    public MultiLanguageString getName() {
	final Degree degree = getDegree();
	final String name = degree.getSigla();
	return MultiLanguageString.i18n().add("pt", name).finish();
    }
}
