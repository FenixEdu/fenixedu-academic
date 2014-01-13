package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class EditPrizeFilter {

    public static final EditPrizeFilter instance = new EditPrizeFilter();

    public void execute(Prize prize) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
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
