/*
 * Created on Mar 7, 2004
 *
 */
package Dominio.finalDegreeWork;

import java.util.List;

import Dominio.IExecutionDegree;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Util.FinalDegreeWorkProposalStatus;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *  
 */
public interface IProposal extends IDomainObject {

    public String getCompanyName();

    public void setCompanyName(String companyName);

    public String getCompanyAdress();

    public void setCompanyAdress(String adress);

    public ITeacher getCoorientator();

    public void setCoorientator(ITeacher coorientator);

    public Integer getCoorientatorsCreditsPercentage();

    public void setCoorientatorsCreditsPercentage(Integer coorientatorsCreditsPercentage);

    public IExecutionDegree getExecutionDegree();

    public void setExecutionDegree(IExecutionDegree executionDegree);

    public TipoCurso getDegreeType();

    public void setDegreeType(TipoCurso degreeType);

    public String getDescription();

    public void setDescription(String description);

    public String getObjectives();

    public void setObjectives(String objectives);

    public String getObservations();

    public void setObservations(String observations);

    public ITeacher getOrientator();

    public void setOrientator(ITeacher orientator);

    public Integer getOrientatorsCreditsPercentage();

    public void setCompanionName(String name);

    public String getCompanionName();

    public void setCompanionMail(String mail);

    public String getCompanionMail();

    public void setCompanionPhone(Integer phone);

    public Integer getCompanionPhone();

    public void setOrientatorsCreditsPercentage(Integer orientatorsCreditsPercentage);

    public String getRequirements();

    public void setRequirements(String requirements);

    public String getTitle();

    public void setTitle(String title);

    public String getUrl();

    public void setUrl(String url);

    public String getDeliverable();

    public void setDeliverable(String deliverable);

    public String getFraming();

    public void setFraming(String framing);

    public String getLocation();

    public void setLocation(String location);

    public Integer getMaximumNumberOfGroupElements();

    public void setMaximumNumberOfGroupElements(Integer maximumNumberOfGroupElements);

    public Integer getMinimumNumberOfGroupElements();

    public void setMinimumNumberOfGroupElements(Integer minimumNumberOfGroupElements);

    public List getBranches();

    public void setBranches(List branches);

    public Integer getProposalNumber();

    public void setProposalNumber(Integer proposalNumber);

    public FinalDegreeWorkProposalStatus getStatus();

    public void setStatus(FinalDegreeWorkProposalStatus status);

    public List getGroupProposals();

    public void setGroupProposals(List groupProposals);

    public IGroup getGroupAttributedByTeacher();

    public void setGroupAttributedByTeacher(IGroup groupAttributedByTeacher);

    public IGroup getGroupAttributed();

    public void setGroupAttributed(IGroup groupAttributed);

}