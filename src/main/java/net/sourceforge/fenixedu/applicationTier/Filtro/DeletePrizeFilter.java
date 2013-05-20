package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DeletePrizeFilter extends Filtro {

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        Prize prize = null;
        if (parameters[0] instanceof Prize) {
            prize = (Prize) parameters[0];
        }
        if (prize != null) {
            if (!prize.isDeletableByUser(person)) {
                throw new NotAuthorizedFilterException("error.not.authorized");
            }
        } else {
            throw new NotAuthorizedFilterException("error.not.authorized");
        }
    }

}
