package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Amin Amirali (amam@mega.ist.utl.pt) Francisco Paulo
 *         (frnp@mega.ist.utl.pt)
 */

public class GuideVO extends VersionedObjectsBase implements IPersistentGuide {

	public Integer generateGuideNumber(final Integer year) {

		int guideNumber = 0;
		final List<Guide> guides = (List) readAll(Guide.class);

		for (final Guide guide : guides) {
			if (guide.getYear().equals(year)) {
				guideNumber = Math.max(guide.getNumber(), guideNumber);
			}
		}
		return ++guideNumber;
	}

	public Guide readByNumberAndYearAndVersion(final Integer number, final Integer year,
			final Integer version) throws ExcepcaoPersistencia {

		final List<Guide> guides = (List) readAll(Guide.class);

		for (final Guide guide : guides) {
			if (guide.getYear().equals(year) && guide.getNumber().equals(number)
					&& guide.getVersion().equals(version)) {
				return guide;
			}
		}
		return null;
	}

	public List readByYear(final Integer year) throws ExcepcaoPersistencia {

		final List<Guide> guides = (List<Guide>) readAll(Guide.class);
		final List<Guide> result = new ArrayList(0);

		if (guides == null || guides.size() == 0) {
			return null;
		}

		for (final Guide guide : guides) {
			if (guide.getYear().equals(year)) {
				result.add(guide);
			}
		}

		return result;
	}

	public Guide readLatestVersion(final Integer year, final Integer number)
			throws ExcepcaoPersistencia {

		final List<Guide> guides = (List<Guide>) readAll(Guide.class);

		if (guides == null || guides.size() == 0) {
			return null;
		}

		for (final Guide guide : guides) {
			if (guide.getYear().equals(year) && guide.getNumber().equals(number)) {
				return guide;
			}
		}

		return null;
	}

	public List readByPerson(final String identificationDocumentNumber,
			final IDDocumentType identificationDocumentType) throws ExcepcaoPersistencia {

		final List<Guide> guides = (List<Guide>) readAll(Guide.class);
		final List<Guide> result = new ArrayList(0);

		if (guides == null || guides.size() == 0) {
			return null;
		}

		for (final Guide guide : guides) {
			if (guide.getPerson().getNumeroDocumentoIdentificacao().equals(identificationDocumentNumber)
					&& guide.getPerson().getIdDocumentType().equals(identificationDocumentType)) {
				result.add(guide);
			}
		}

		return result;
	}

	public List readByYearAndState(Integer guideYear, GuideState situationOfGuide)
			throws ExcepcaoPersistencia {
		final List<Guide> guides = (List<Guide>) readAll(Guide.class);
		final List<Guide> result = new ArrayList(0);

		if (guides == null || guides.size() == 0) {
			return result;
		}

		for (final Guide guide : guides) {
			if (guide.getYear().equals(guideYear)) {
				for (GuideSituation guideSituation : guide.getGuideSituations()) {
					if (guideSituation.getState().equals(State.ACTIVE)
							&& guideSituation.getSituation().equals(situationOfGuide)) {
						result.add(guide);
					}
				}
			}
		}

		return result;
	}

	public List readByNumberAndYear(final Integer number, final Integer year)
			throws ExcepcaoPersistencia {

		final List<Guide> guides = (List<Guide>) readAll(Guide.class);
		final List<Guide> result = new ArrayList(0);

		if (guides == null || guides.size() == 0) {
			return null;
		}

		for (final Guide guide : guides) {
			if (guide.getNumber().equals(number) && guide.getYear().equals(year)) {
				result.add(guide);
			}
		}

		return result;

	}

}