package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.IPrice;
import net.sourceforge.fenixedu.domain.Price;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrice;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PriceVO extends VersionedObjectsBase implements IPersistentPrice {

	public List readAll() throws ExcepcaoPersistencia {
		return (List) readAll(Price.class);
	}

	public List readByGraduationType(GraduationType graduationType) throws ExcepcaoPersistencia {
		List<IPrice> priceList = (List<IPrice>) readAll(Price.class);
		List result = new ArrayList();

		for (IPrice price : priceList) {
			if (price.getGraduationType().equals(graduationType)) {
				result.add(price);
			}
		}

		return result;
	}

	public IPrice readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
			DocumentType documentType, String description) throws ExcepcaoPersistencia {

		List<IPrice> priceList = (List<IPrice>) readAll(Price.class);
		List result = new ArrayList();

		for (IPrice price : priceList) {
			if (price.getGraduationType().equals(graduationType)
					&& price.getDocumentType().equals(documentType)
					&& price.getDescription().equals(description)) {
				return price;
			}
		}

		return null;
	}

	public List readByGraduationTypeAndDocumentType(GraduationType graduationType,
			DocumentType documentType) throws ExcepcaoPersistencia {
		List<IPrice> priceList = (List<IPrice>) readAll(Price.class);
		List result = new ArrayList();

		for (IPrice price : priceList) {
			if (price.getGraduationType().equals(graduationType)
					&& price.getDocumentType().equals(documentType)) {
				result.add(price);
			}
		}

		return result;
	}

	public List readByGraduationTypeAndDocumentType(GraduationType graduationType, List types)
			throws ExcepcaoPersistencia {

		List<IPrice> priceList = (List<IPrice>) readAll(Price.class);
		List result = new ArrayList();

		for (IPrice price : priceList) {
			if (price.getGraduationType().equals(graduationType)) {
				for (GraduationType type : (List<GraduationType>) types) {
					if (price.getGraduationType().equals(type)) {
						result.add(price);
					}
				}
			}
		}

		return result;
	}
}