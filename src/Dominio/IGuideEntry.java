package Dominio;

import Util.DocumentType;
import Util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IGuideEntry extends IDomainObject{
  public Integer getQuantity();
  public GraduationType getGraduationType();
  public Double getPrice();
  public String getDescription();
  public IGuide getGuide();
  public DocumentType getDocumentType();

  public void setQuantity(Integer quantity);
  public void setGraduationType(GraduationType graduationType);
  public void setPrice(Double price);
  public void setDescription(String description);
  public void setGuide(IGuide guide);
  public void setDocumentType(DocumentType documentType);
}
