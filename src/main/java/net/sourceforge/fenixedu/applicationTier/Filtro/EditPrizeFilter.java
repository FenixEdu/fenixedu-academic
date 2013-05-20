package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class EditPrizeFilter extends Filtro {

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        Prize prize = null;
        if (parameters[1] instanceof Prize) {
            prize = (Prize) parameters[1];
        }
        if (prize != null) {
            if (!prize.isEditableByUser(person)) {
                throw new NotAuthorizedFilterException("error.not.authorized");
            }
        } else {
            throw new NotAuthorizedFilterException("error.not.authorized");
        }

    }

}
