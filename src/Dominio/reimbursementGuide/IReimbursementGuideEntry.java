/*
 * Created on 18/Mar/2004
 *  
 */
package Dominio.reimbursementGuide;

import Dominio.IDomainObject;
import Dominio.IGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */

public interface IReimbursementGuideEntry extends IDomainObject
{
	/**
	 * @return Returns the guideEntry.
	 */
	public abstract IGuideEntry getGuideEntry();
	/**
	 * @param guideEntry
	 *            The guideEntry to set.
	 */
	public abstract void setGuideEntry(IGuideEntry guideEntry);
	/**
	 * @return Returns the justification.
	 */
	public abstract String getJustification();
	/**
	 * @param justification
	 *            The justification to set.
	 */
	public abstract void setJustification(String justification);
	/**
	 * @return Returns the reimbursementGuide.
	 */
	public abstract IReimbursementGuide getReimbursementGuide();
	/**
	 * @param reimbursementGuide
	 *            The reimbursementGuide to set.
	 */
	public abstract void setReimbursementGuide(IReimbursementGuide reimbursementGuide);
	/**
	 * @return Returns the value.
	 */
	public abstract Double getValue();
	/**
	 * @param value
	 *            The value to set.
	 */
	public abstract void setValue(Double value);
}
