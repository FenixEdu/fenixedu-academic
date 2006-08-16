package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;


public class ResultPublication extends ResultPublication_Base {
    
    public ResultPublication() {
        super();
    }
    
    public void delete()
    {
        removePublisher();
        removeOrganization();
        super.delete();
    }
    
    public Boolean hasResultPublicationRole()
    {
        if((this instanceof Book) || (this instanceof BookPart) || (this instanceof Inproceedings))
            return true;
        return false;
    }
    
    public List<ResultParticipation> getAuthors() {
    	ArrayList<ResultParticipation> authors = new ArrayList<ResultParticipation>();
    	for (ResultParticipation participation : this.getResultParticipations()) {
			if(participation.getResultParticipationRole().equals(ResultParticipationRole.Author))
				authors.add(participation);
		}
    	return authors;
    }
    
    public List<ResultParticipation> getEditors() {
    	ArrayList<ResultParticipation> editors = new ArrayList<ResultParticipation>();
    	for (ResultParticipation participation : this.getResultParticipations()) {
			if(participation.getResultParticipationRole().equals(ResultParticipationRole.Editor))
				editors.add(participation);
		}
    	return  editors;
    }

    public enum ScopeType {
        LOCAL,
        NATIONAL,
        INTERNATIONAL;
    }

}
