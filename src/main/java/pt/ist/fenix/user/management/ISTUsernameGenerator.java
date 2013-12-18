package pt.ist.fenix.user.management;

import org.fenixedu.bennu.user.management.UsernameGenerator;

public class ISTUsernameGenerator extends UsernameGenerator<Object> {

    private final IstUsernameCounter counter;

    ISTUsernameGenerator(IstUsernameCounter counter) {
        this.counter = counter;
    }

    @Override
    protected String doGenerate(Object ignored) {
        return "ist" + counter.getNext();
    }

}
