package DataBeans.gesdis;


/**
 * @author jorge
 */
public class ItemView {
  protected String name;
  protected String information;
  protected Boolean urgent;
    
  public ItemView(String name,String information,Boolean urgent) {
    this.name = name;
	this.information = information;
	this.urgent = urgent;
  }    

  public String getNome() {
    return name;
  }

  public String getInformacao() {
    return information;
  }
    
  public Boolean getUrgente() {
    return urgent;
  }
}
