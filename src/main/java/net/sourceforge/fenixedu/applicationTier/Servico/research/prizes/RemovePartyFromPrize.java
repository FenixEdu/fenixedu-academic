package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.Filtro.EditPrizeFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.Prize;
import pt.ist.fenixframework.Atomic;

public class RemovePartyFromPrize {

    protected void run(Party party, Prize prize) {
        prize.getParties().remove(party);
    }

    // Service Invokers migrated from Berserk

    private static final RemovePartyFromPrize serviceInstance = new RemovePartyFromPrize();

    @Atomic
    public static void runRemovePartyFromPrize(Party party, Prize prize) throws NotAuthorizedException {
        EditPrizeFilter.instance.execute(prize);
        serviceInstance.run(party, prize);
    }

}