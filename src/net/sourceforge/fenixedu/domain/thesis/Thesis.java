package net.sourceforge.fenixedu.domain.thesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Thesis extends Thesis_Base {
    
    public Thesis() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());

        setCreation(new DateTime());
        setState(ThesisState.DRAFT);
    }
    
    public void delete() {
        if (getState() != ThesisState.DRAFT) {
            throw new DomainException("thesis.delete.notDraft");
        }
        
        removeRootDomainObject();

        removeOrientator();
        removeCoorientator();
        removePresident();
        
        getVowels().clear();
        
        removeDissertation();
        removeExtendedAbstract();
        
        removeEnrolment();
        
        deleteDomainObject();
    }
    
    public static Collection<Thesis> getDraftThesis() {
        List<Thesis> theses = new ArrayList<Thesis>();
        
        for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
            if (thesis.getState() == ThesisState.DRAFT) {
                theses.add(thesis);
            }
        }
        
        return theses;
    }
}
