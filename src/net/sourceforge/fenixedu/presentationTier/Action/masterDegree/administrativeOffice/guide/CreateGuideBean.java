package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

public class CreateGuideBean implements Serializable {

	private InfoGuide infoGuide;
	private Integer requesterNumber;
	private Specialization graduationType;
	private GuideRequester requester;
	private List<InfoPrice> infoPrices;

	public CreateGuideBean(InfoGuide infoGuide, Integer requesterNumber, Specialization graduationType, GuideRequester requester) {
		super();
		this.graduationType = graduationType;
		this.infoGuide = infoGuide;
		this.requester = requester;
		this.requesterNumber = requesterNumber;
	}

	public InfoGuide getInfoGuide() {
		return infoGuide;
	}

	public void setInfoGuide(InfoGuide infoGuide) {
		this.infoGuide = infoGuide;

	}

	public Integer getRequesterNumber() {
		return requesterNumber;
	}

	public void setRequesterNumber(Integer requesterNumber) {
		this.requesterNumber = requesterNumber;
	}

	public Specialization getGraduationType() {
		return graduationType;
	}

	public void setGraduationType(Specialization graduationType) {
		this.graduationType = graduationType;
	}

	public GuideRequester getRequester() {
		return requester;
	}

	public void setRequester(GuideRequester requester) {
		this.requester = requester;
	}

	public boolean isForCandidateRequester() {
		return getRequester() == GuideRequester.CANDIDATE;
	}

	public List<InfoPrice> getInfoPrices() {
		return infoPrices;
	}

	public void setInfoPrices(List<InfoPrice> infoPrices) {
		this.infoPrices = infoPrices;
	}

}
