<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<html:xhtml/>

<logic:present role="PERSON">

	<em><bean:message key="label.person.main.title" /></em>
	<h2><bean:message key="label.validate.email"/></h2>


	<div class="infoop2 mtop1">
		<bean:message key="message.validate.email"/>
	</div>
			

	<p class="mtop2"><strong><bean:message key="message.validate.email.prefix"/></strong></p>
	
	
	<fr:form action="/validateEmail.do?method=redirect">
		<fr:create id="validateEmail" schema="validate.email"
				type="net.sourceforge.fenixedu.presentationTier.Action.person.ValidateEmailDA$ValidateEmailForm">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
 					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="search" path="/announcements/boards.do?method=search"/>
		   	</fr:layout>
		</fr:create>
		
	<html:submit><bean:message key="label.submit"/></html:submit>
	
	</fr:form>
	
	
</logic:present>