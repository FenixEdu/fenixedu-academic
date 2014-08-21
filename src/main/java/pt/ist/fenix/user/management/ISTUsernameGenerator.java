package pt.ist.fenix.user.management;

import org.fenixedu.bennu.core.domain.User.UsernameGenerator;
import org.fenixedu.bennu.core.domain.UserProfile;

public class ISTUsernameGenerator implements UsernameGenerator {

    private final IstUsernameCounter counter;

    ISTUsernameGenerator(IstUsernameCounter counter) {
        this.counter = counter;
    }

    @Override
    public String doGenerate(UserProfile parameter) {
        return "ist" + counter.getNext();
    }

}
