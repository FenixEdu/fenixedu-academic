/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 *
 * 
 */
public class InfoSiteObjectives implements ISiteComponent {

private String generalObjectives;
private String operacionalObjectives;

/**
 * @return
 */
public String getGeneralObjectives() {
	return generalObjectives;
}

/**
 * @return
 */
public String getOperacionalObjectives() {
	return operacionalObjectives;
}

/**
 * @param string
 */
public void setGeneralObjectives(String string) {
	generalObjectives = string;
}

/**
 * @param string
 */
public void setOperacionalObjectives(String string) {
	operacionalObjectives = string;
}

}
