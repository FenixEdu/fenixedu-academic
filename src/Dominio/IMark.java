package Dominio;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public interface IMark extends IDomainObject {

    public String getMark();

    public String getPublishedMark();

    public IAttends getAttend();

    public IEvaluation getEvaluation();

    public Integer getKeyAttend();

    public Integer getKeyEvaluation();

    public void setMark(String mark);

    public void setPublishedMark(String publishedMark);

    public void setAttend(IAttends attend);

    public void setEvaluation(IEvaluation evaluation);

    public void setKeyAttend(Integer keyAttend);

    public void setKeyEvaluation(Integer keyEvaluation);

}