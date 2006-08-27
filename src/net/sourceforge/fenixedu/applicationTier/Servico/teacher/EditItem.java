package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditItem extends Service {

	public Boolean run(Integer infoExecutionCourseCode, Integer itemCode, InfoItem newInfoItem)
			throws FenixServiceException, ExcepcaoPersistencia {
		Item item = rootDomainObject.readItemByOID(itemCode);
		if (item == null) {
			throw new ExistingServiceException();
		}

		if (newInfoItem.getItemOrder() == -2)
			newInfoItem.setItemOrder(Integer.valueOf(item.getSection().getAssociatedItemsCount() - 1));

		int diffOrder = newInfoItem.getItemOrder() - item.getItemOrder();
		if (diffOrder < 0)
			newInfoItem.setItemOrder(newInfoItem.getItemOrder() + 1);

		item.edit(newInfoItem.getName(), newInfoItem.getInformation(), newInfoItem.getItemOrder());

		return Boolean.TRUE;
	}

}
