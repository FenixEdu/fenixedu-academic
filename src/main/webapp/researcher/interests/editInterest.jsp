<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="label.researchPortal" bundle="RESEARCHER_RESOURCES"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="title.edit.interest"/></h2>

<fr:edit id="input" name="interest" schema="researchInterest.simpleCreate" 
	action="/interests/interestsManagement.do?method=prepare">	  
	<fr:destination name="cancel" path="/interests/interestsManagement.do?method=prepare"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thmiddle thlight thtop mbottom1"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
