package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DeletePrizeFilter {

    public static final DeletePrizeFilter instance = new DeletePrizeFilter();

    public void execute(Prize prize) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        if (prize != null) {
            if (!prize.isDeletableByUser(person)) {
                throw new NotAuthorizedFilterException("error.not.authorized");
            }
        } else {
            throw new NotAuthorizedFilterException("error.not.authorized");
        }
    }

}
