package net.sourceforge.fenixedu.domain.phd.alert;

public class PhdPublicPresentationSeminarAlert extends PhdPublicPresentationSeminarAlert_Base {

    public PhdPublicPresentationSeminarAlert() {
	super();
    }

    @Override
    public boolean isToFire() {
	throw new RuntimeException("TODO!!");
    }

    @Override
    protected boolean isToDiscard() {
	throw new RuntimeException("TODO!!");
    }

    @Override
    public boolean isSystemAlert() {
	return true;
    }

    @Override
    public String getDescription() {
	throw new RuntimeException("TODO!!");
    }

    @Override
    protected void generateMessage() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isToSendMail() {
	return true;
    }

}
