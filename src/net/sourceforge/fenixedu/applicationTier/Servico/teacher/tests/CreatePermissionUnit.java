package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.renderers.CreateObjects;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.tests.NewPermissionUnit;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;

public class CreatePermissionUnit extends CreateObjects {

    @Override
    protected void afterRun(Collection<Object> touchedObjects) {
        for(Object touchedObject : touchedObjects) {
            if(touchedObject instanceof NewPermissionUnit) {
                doIntegrityChecks((NewPermissionUnit) touchedObject);
            }
        }
    }
    
    private void doIntegrityChecks(NewPermissionUnit permissionUnit) {
        Party party = permissionUnit.getParty();
        NewQuestion question = permissionUnit.getQuestion();
        
        if(question.getOwner().equals(party)) {
            throw new DomainException("could.not.add.owner");
        }
        
        List<NewPermissionUnit> permissionUnits = question.getPermissionUnits();
        
        for(NewPermissionUnit oldPermissionUnit : permissionUnits) {
            if(!oldPermissionUnit.equals(permissionUnit) && oldPermissionUnit.getParty().equals(party)) {
                oldPermissionUnit.delete();
            }
        }
    }

}
