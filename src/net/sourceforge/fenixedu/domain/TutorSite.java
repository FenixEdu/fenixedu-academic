package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.TutorUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TutorSite extends TutorSite_Base {
    
    public TutorSite(TutorUnit unit) {
        super();
        
        setUnit(unit);
    }
    
    @Override
    public IGroup getOwner() {
    	return new GroupUnion(
    			new FixedSetGroup(getManagers())
		);
    }

    @Override
    public void appendReversePathPart(final StringBuilder stringBuilder) {
    }
    
    @Override
    public MultiLanguageString getName() {
	return new MultiLanguageString("");
    }

}
