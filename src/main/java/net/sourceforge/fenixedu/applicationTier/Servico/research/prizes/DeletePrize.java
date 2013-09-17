package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;


import net.sourceforge.fenixedu.applicationTier.Filtro.DeletePrizeFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.research.Prize;
import pt.ist.fenixframework.Atomic;

public class DeletePrize {

    protected void run(Prize prize) {
        prize.delete();
    }
    // Service Invokers migrated from Berserk

    private static final DeletePrize serviceInstance = new DeletePrize();

    @Atomic
    public static void runDeletePrize(Prize prize) throws NotAuthorizedException {
        DeletePrizeFilter.instance.execute(prize);
        serviceInstance.run(prize);
    }

}