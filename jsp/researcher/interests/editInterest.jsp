<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	
	<em><bean:message key="label.researchPortal" bundle="RESEARCHER_RESOURCES"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="title.edit.interest"/></h2>
	
	<fr:edit id="input" name="interest" schema="researchInterest.simpleCreate" 
		action="/interests/interestsManagement.do?method=prepare">	  
		<fr:destination name="cancel" path="/interests/interestsManagement.do?method=prepare"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thmiddle thlight thtop mbottom1"/>
		</fr:layout>
	</fr:edit>

</logic:present>