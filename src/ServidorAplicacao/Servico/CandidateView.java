package ServidorAplicacao.Servico;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateSituation;
import ServidorAplicacao.ICandidateView;
import Util.SituationName;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateView implements ICandidateView {
	private List infoApplicationInfos;
	

	public CandidateView() {
		this.infoApplicationInfos = null;
	}

	public CandidateView(List infoApplicationInfos) {
		this.infoApplicationInfos = infoApplicationInfos;
	}

	public boolean changeablePersonalInfo(){
//		Iterator iteratorApplications = this.infoApplicationInfos.iterator();
//		while(iteratorApplications.hasNext()){
			Iterator iteratorSituations = this.infoApplicationInfos.iterator();
			while(iteratorSituations.hasNext()){
				InfoCandidateSituation infoCandidateSituation = (InfoCandidateSituation) iteratorSituations.next();
		
				// If the active Candidate Situation is diferent from Pending, them the Candidate cannot Change his Personal
				// Information. 
				// Note: In this list there are only Active Situations !!

				if (!infoCandidateSituation.getSituation().equals(SituationName.PENDENTE_STRING))
					return false;
			}
		
		return true;
	}


	/**
	 * @return
	 */
	public List getInfoApplicationInfos() {
		return infoApplicationInfos;
	}

	/**
	 * @param list
	 */
	public void setInfoApplicationInfos(List list) {
		infoApplicationInfos = list;
	}

}