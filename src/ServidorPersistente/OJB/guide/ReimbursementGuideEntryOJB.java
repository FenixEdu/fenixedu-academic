/*
 * Created on 19/Mar/2004
 *  
 */
package ServidorPersistente.OJB.guide;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IGuideEntry;
import Dominio.reimbursementGuide.ReimbursementGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class ReimbursementGuideEntryOJB
	extends ObjectFenixOJB
	implements IPersistentReimbursementGuideEntry
{

	/**
	 *  
	 */
	public ReimbursementGuideEntryOJB()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.guide.IPersistentReimbursementGuideEntry#readByGuideEntry(Dominio.IGuideEntry)
	 */
	public List readByGuideEntry(IGuideEntry guideEntry) throws ExcepcaoPersistencia
	{
		Criteria crit = new Criteria();
		crit.addEqualTo("guideEntry.idInternal", guideEntry.getIdInternal());
		return queryList(ReimbursementGuideEntry.class, crit);

	}

}
