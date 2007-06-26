<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.create.prize"/></h2>
	
<fr:edit id="createPrize" name="bean" schema="create.prize" action="/prizes/prizeManagement.do?method=createPrize">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="/prizes/prizeManagement.do?method=listPrizes"/>
</fr:edit>