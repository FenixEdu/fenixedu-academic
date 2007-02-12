package net.sourceforge.fenixedu.dataTransferObject.research.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.activity.Participation;

public class ResearchActivityEditionBean implements Serializable {
	private List<Participation> participations;
	private List<Participation> otherParticipations;
	
    public List<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(List<Participation> participations) {
		this.participations = participations;
	}

	public List<Participation> getOtherParticipations() {
		return otherParticipations;
	}

	public void setOtherParticipations(List<Participation> otherParticipations) {
		this.otherParticipations = otherParticipations;
	}  
	
	public List<PartyParticipationsBean> getOtherParticipationsAsList() {
		Map<Party, List<Participation>> participationsMap = new HashMap<Party, List<Participation>>();
		for(Participation participation : this.getOtherParticipations()) {
			Party party = participation.getParty();
			if(participationsMap.containsKey(party)) {
				participationsMap.get(party).add(participation);
			}
			else {
				List<Participation> participations = new ArrayList<Participation>();
				participations.add(participation);
				participationsMap.put(participation.getParty(), participations);
			}
		}
		
		List<PartyParticipationsBean> participationList = new ArrayList<PartyParticipationsBean> ();
		for(Party party : participationsMap.keySet()) {
			participationList.add(new PartyParticipationsBean(party, participationsMap.get(party)));
		}
		return participationList;
	}
	
	public class PartyParticipationsBean implements Serializable {
		
		DomainReference<Party> party;
		List<Participation> participations;

		public PartyParticipationsBean() {
			this.party = new DomainReference<Party>(null);
			this.participations = new ArrayList<Participation> ();
		}
		
		public PartyParticipationsBean(Party party, List<Participation> participations) {
			this.party = new DomainReference<Party>(party);
			this.participations = participations;
		}
		
		public Party getParty() {
			return this.party.getObject();
		}
		
		public List<Participation> getParticipations() {
			return participations;
		}
		
	}
}
