/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
