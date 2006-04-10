<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	

	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="title.edit.interest"/></h2><br/>
			
	<fr:edit id="input" name="interest" schema="researchInterest.simpleCreate" 
		action="/interests/interestsManagement.do?method=prepare">	   
	</fr:edit>

	<br/>	
	<html:link module="/researcher" page="/interests/interestsManagement.do?method=prepare">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.back" />
	</html:link>
	
</logic:present>