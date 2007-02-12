package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveResearchActivityParticipation extends Service {

    public void run(Person person, Event event) throws ExcepcaoPersistencia, FenixServiceException {
    	List<Participation> participations = person.getParticipations();
    	
    	for(Participation participation : participations){
    		if(participation.getResearchActivity().equals(event)){
    			participation.delete();
    		}
    	}
        
        if(!event.hasAnyParticipations())
        	event.delete();
    }
    
    public void run(Person person, ScientificJournal journal) throws ExcepcaoPersistencia, FenixServiceException {
    	List<Participation> participations = person.getParticipations();
    	
    	for(Participation participation : participations){
    		if(participation.getResearchActivity().equals(journal)){
    			participation.delete();
    		}
    	}
        
        if(!journal.hasAnyParticipations())
        	journal.delete();
    }
    
    public void run(Person person, Cooperation cooperation) throws ExcepcaoPersistencia, FenixServiceException {
    	List<Participation> participations = person.getParticipations();
    	
    	for(Participation participation : participations){
    		if(participation.getResearchActivity().equals(cooperation)){
    			participation.delete();
    		}
    	}
        
        if(!cooperation.hasAnyParticipations())
        	cooperation.delete();
    }
    
    public void run(Person person, Participation participation, Event event) throws ExcepcaoPersistencia, FenixServiceException {        
        if(participation == null){
            throw new FenixServiceException();
        }
        
        participation.delete();   
        
        if(!event.hasMoreParticipationsFrom(person))
        	throw new DomainException("error.researcher.ResearchActivityParticipation.invalidParticipationsCount");
    }
    
    public void run(Person person, Participation participation, ScientificJournal journal) throws ExcepcaoPersistencia, FenixServiceException {        
        if(participation == null){
            throw new FenixServiceException();
        }
        
        participation.delete();
        
        if(!journal.hasMoreParticipationsFrom(person))
        	throw new DomainException("error.researcher.ResearchActivityParticipation.invalidParticipationsCount");
    }
}


