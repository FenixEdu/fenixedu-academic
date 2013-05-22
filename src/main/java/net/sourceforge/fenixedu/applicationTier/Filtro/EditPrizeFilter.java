package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonNameBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class EditPrizeFilter {

    public static final EditPrizeFilter instance = new EditPrizeFilter();

    public void execute(PersonNameBean bean, Prize prize) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        if (prize != null) {
            if (!prize.isEditableByUser(person)) {
                throw new NotAuthorizedException("error.not.authorized");
            }
        } else {
            throw new NotAuthorizedException("error.not.authorized");
        }

    }

}
