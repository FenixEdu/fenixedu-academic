package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.Contributor;

public interface IPersistentContributor extends IPersistentObject {

    public Contributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia;

}
