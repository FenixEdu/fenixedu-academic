/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.util.GuideRequester;
import net.sourceforge.fenixedu.util.PaymentType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuide extends InfoObject {

    private Integer number;

    private Integer year;

    private Double total;

    private String remarks;

    private InfoPerson infoPerson;

    private InfoContributor infoContributor;

    private List infoGuideEntries;

    private GuideRequester guideRequester;

    private InfoExecutionDegree infoExecutionDegree;

    private PaymentType paymentType;

    private Date creationDate;

    private Integer version;

    private List infoGuideSituations;

    private InfoGuideSituation infoGuideSituation;

    private Date paymentDate;

    private List infoReimbursementGuides;

    public InfoGuide() {
    }

    public InfoGuide(Integer number, Integer year, Double total, String remarks, InfoPerson infoPerson,
            InfoContributor infoContributor, GuideRequester guideRequester,
            InfoExecutionDegree infoExecutionDegree, PaymentType paymentType, Date creationDate,
            Integer version) {
        this.number = number;
        this.year = year;
        this.total = total;
        this.remarks = remarks;
        this.infoPerson = infoPerson;
        this.infoContributor = infoContributor;
        this.guideRequester = guideRequester;
        this.infoExecutionDegree = infoExecutionDegree;
        this.paymentType = paymentType;
        this.creationDate = creationDate;
        this.version = version;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoGuide) {
            InfoGuide guide = (InfoGuide) obj;

            resultado = getNumber().equals(guide.getNumber()) && getYear().equals(guide.getYear());
        }

        return resultado;
    }

    public String toString() {
        String result = "[INFO_GUIDE";
        result += ", number=" + number;
        result += ", year=" + year;
        result += ", person=" + infoPerson;
        result += ", contributor=" + infoContributor;
        result += ", total=" + total;
        result += ", remarks=" + remarks;
        result += ", infoGuideEntries=" + infoGuideEntries;
        result += ", guideRequester=" + guideRequester;
        result += ", infoExecutionDegree=" + infoExecutionDegree;
        result += ", paymentType=" + paymentType;
        result += ", creationDate=" + creationDate;
        result += ", version=" + version;
        result += ", infoGuideSituation=" + infoGuideSituation;
        result += ", payment Date=" + paymentDate;
        result += "]";
        return result;
    }

    /**
     * @return
     */
    public GuideRequester getGuideRequester() {
        return guideRequester;
    }

    /**
     * @return
     */
    public InfoContributor getInfoContributor() {
        return infoContributor;
    }

    /**
     * @return
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @return
     */
    public List getInfoGuideEntries() {
        return infoGuideEntries;
    }

    /**
     * @return
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @return
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @return
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @return
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param requester
     */
    public void setGuideRequester(GuideRequester requester) {
        guideRequester = requester;
    }

    /**
     * @param contributor
     */
    public void setInfoContributor(InfoContributor contributor) {
        infoContributor = contributor;
    }

    /**
     * @param degree
     */
    public void setInfoExecutionDegree(InfoExecutionDegree degree) {
        infoExecutionDegree = degree;
    }

    /**
     * @param list
     */
    public void setInfoGuideEntries(List list) {
        infoGuideEntries = list;
    }

    /**
     * @param person
     */
    public void setInfoPerson(InfoPerson person) {
        infoPerson = person;
    }

    /**
     * @param integer
     */
    public void setNumber(Integer integer) {
        number = integer;
    }

    /**
     * @param string
     */
    public void setRemarks(String string) {
        remarks = string;
    }

    /**
     * @param double1
     */
    public void setTotal(Double double1) {
        total = double1;
    }

    /**
     * @param integer
     */
    public void setYear(Integer integer) {
        year = integer;
    }

    /**
     * @return
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * @param type
     */
    public void setPaymentType(PaymentType type) {
        paymentType = type;
    }

    /**
     * @return
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param date
     */
    public void setCreationDate(Date date) {
        creationDate = date;
    }

    /**
     * @param integer
     */
    public void setVersion(Integer integer) {
        version = integer;
    }

    /**
     * @return
     */
    public List getInfoGuideSituations() {
        return infoGuideSituations;
    }

    /**
     * @param list
     */
    public void setInfoGuideSituations(List list) {
        infoGuideSituations = list;
    }

    /**
     * @return
     */
    public InfoGuideSituation getInfoGuideSituation() {
        return infoGuideSituation;
    }

    /**
     * @param situation
     */
    public void setInfoGuideSituation(InfoGuideSituation situation) {
        infoGuideSituation = situation;
    }

    /**
     * @return
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param date
     */
    public void setPaymentDate(Date date) {
        paymentDate = date;
    }

    /**
     * @return Returns the reimbursementGuides.
     */
    public List getInfoReimbursementGuides() {
        return infoReimbursementGuides;
    }

    /**
     * @param reimbursementGuides
     *            The reimbursementGuides to set.
     */
    public void setInfoReimbursementGuides(List reimbursementGuides) {
        this.infoReimbursementGuides = reimbursementGuides;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setCreationDate(guide.getCreationDate());
            setNumber(guide.getNumber());
            setPaymentDate(guide.getPaymentDate());
            setPaymentType(guide.getPaymentType());
            setRemarks(guide.getRemarks());
            setTotal(guide.getTotal());
            setVersion(guide.getVersion());
            setYear(guide.getYear());
        }
    }

    public static InfoGuide newInfoFromDomain(IGuide guide) {
        InfoGuide infoGuide = null;
        if (guide != null) {
            infoGuide = new InfoGuide();
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }
}