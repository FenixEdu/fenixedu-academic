package net.sourceforge.fenixedu.domain.candidacy;

public class GenericApplicationComment extends GenericApplicationComment_Base {

    public GenericApplicationComment() {
        super();
    }

    @Deprecated
    public boolean hasGenericApplication() {
        return getGenericApplication() != null;
    }

}
