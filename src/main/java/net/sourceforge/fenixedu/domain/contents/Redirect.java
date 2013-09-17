package net.sourceforge.fenixedu.domain.contents;

public class Redirect extends Redirect_Base {

    public Redirect() {
        super();
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

}
