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
private String generalObjectivesEn;
private String operacionalObjectivesEn;


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

/**
 * @return
 */
public String getGeneralObjectivesEn() {
	return generalObjectivesEn;
}

/**
 * @return
 */
public String getOperacionalObjectivesEn() {
	return operacionalObjectivesEn;
}

/**
 * @param string
 */
public void setGeneralObjectivesEn(String string) {
	generalObjectivesEn = string;
}

/**
 * @param string
 */
public void setOperacionalObjectivesEn(String string) {
	operacionalObjectivesEn = string;
}

}
