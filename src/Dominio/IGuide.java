package Dominio;

import java.util.Date;
import java.util.List;

import Util.GuideRequester;
import Util.PaymentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IGuide extends IDomainObject {
    public Integer getNumber();

    public Integer getYear();

    public Double getTotal();

    public String getRemarks();

    public IPessoa getPerson();

    public IContributor getContributor();

    public List getGuideEntries();

    public GuideRequester getGuideRequester();

    public ICursoExecucao getExecutionDegree();

    public PaymentType getPaymentType();

    public Date getCreationDate();

    public Integer getVersion();

    public List getGuideSituations();

    public Date getPaymentDate();

    public IGuideSituation getActiveSituation();

    public List getReimbursementGuides();

    public void setNumber(Integer number);

    public void setYear(Integer year);

    public void setTotal(Double total);

    public void setRemarks(String remarks);

    public void setPerson(IPessoa person);

    public void setContributor(IContributor contributor);

    public void setGuideEntries(List guideEntries);

    public void setGuideRequester(GuideRequester guideRequester);

    public void setExecutionDegree(ICursoExecucao executionDegree);

    public void setPaymentType(PaymentType paymentType);

    public void setCreationDate(Date creationDate);

    public void setVersion(Integer version);

    public void setGuideSituations(List guideSituations);

    public void setPaymentDate(Date paymentDate);

    public void setReimbursementGuides(List reimbursementGuides);
}