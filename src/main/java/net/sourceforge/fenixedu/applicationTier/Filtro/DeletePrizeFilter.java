package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class DeletePrizeFilter {

    public static final DeletePrizeFilter instance = new DeletePrizeFilter();

    public void execute(Prize prize) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        Person person = userView.getPerson();

        if (prize != null) {
            if (!prize.isDeletableByUser(person)) {
                throw new NotAuthorizedException("error.not.authorized");
            }
        } else {
            throw new NotAuthorizedException("error.not.authorized");
        }
    }

}
