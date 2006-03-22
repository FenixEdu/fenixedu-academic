package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class RootDomainObject extends RootDomainObject_Base {

    private static RootDomainObject instance = null;

    public static synchronized void init() {
        if (instance == null) {
            final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
            try {
                suportePersistente.iniciarTransaccao();
                final IPersistentObject persistentObject = suportePersistente.getIPersistentObject();
                instance = persistentObject.readRootDomainObject();
                suportePersistente.confirmarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                throw new Error("error.root.domain.object.not.retrieved", e);
            }
        }
    }
    
    public static RootDomainObject getInstance() {
        if (instance == null) {
            init();
        } 
        return instance;
    }

    private RootDomainObject() {
    }
 
    public static List<Person> readAllPersons() {
        List<Person> allPersons = new ArrayList<Person>();
        for (Party party : getInstance().getPartys()) {
           if (party instanceof Person) {
               Person person = (Person) party;
               allPersons.add(person);
           }
        }
        return allPersons;
    }
    
    public static List<Unit> readAllUnits() {
        List<Unit> allUnits = new ArrayList<Unit>();
        for (Party party : getInstance().getPartys()) {
           if (party instanceof Unit) {
               Unit unit = (Unit) party;
               allUnits.add(unit);
           }
        }
        return allUnits;
    }

	public static void initTests() {
		instance = new RootDomainObject();		
	}

}
