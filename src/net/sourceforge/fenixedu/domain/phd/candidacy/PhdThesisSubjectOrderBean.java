package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.phd.ThesisSubject;

public class PhdThesisSubjectOrderBean implements Serializable {

    public static Comparator<PhdThesisSubjectOrderBean> COMPARATOR_BY_ORDER = new Comparator<PhdThesisSubjectOrderBean>() {
	@Override
	public int compare(PhdThesisSubjectOrderBean bean1, PhdThesisSubjectOrderBean bean2) {
	    return bean1.getOrder() - bean2.getOrder();
	}
    };

    public PhdThesisSubjectOrderBean(int order, ThesisSubject thesisSubject) {
	this.order = order;
	this.thesisSubject = thesisSubject;
    }

    private static final long serialVersionUID = 1L;

    private int order;

    private ThesisSubject thesisSubject;

    public int getOrder() {
	return order;
    }

    public void setOrder(int order) {
	this.order = order;
    }

    public ThesisSubject getThesisSubject() {
	return thesisSubject;
    }

    public void setThesisSubject(ThesisSubject thesisSubject) {
	this.thesisSubject = thesisSubject;
    }

    public void increaseOrder() {
	order++;
    }

    public void decreaseOrder() {
	order--;
    }
}
