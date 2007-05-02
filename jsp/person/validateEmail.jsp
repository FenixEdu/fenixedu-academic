<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<html:xhtml/>

<logic:present role="PERSON">
	<fr:create id="validateEmail" schema="validate.email"
			type="net.sourceforge.fenixedu.presentationTier.Action.person.ValidateEmailDA$ValidateEmailForm"
			action="/validateEmail.do?method=redirect">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
 				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:destination name="search" path="/announcements/boards.do?method=search"/>
	   	</fr:layout>
	</fr:create>
</logic:present>