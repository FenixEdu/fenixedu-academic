package Dominio;

import java.util.Date;

import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IGuideSituation {
  
  public GuideSituation getSituation();
  public Date getDate();
  public String getRemarks();
  public IGuide getGuide();
  public State getState();

  public void setSituation(GuideSituation situation);
  public void setDate(Date date);
  public void setRemarks(String remarks);
  public void setGuide(IGuide guide);
  public void setState(State state);
}
