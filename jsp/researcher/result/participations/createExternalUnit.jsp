<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<bean:define id="result" name="result"/>
<bean:define id="resultId" name="result" property="idInternal"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
<bean:define id="path" value="<%= "/resultParticipations/createUnit.do?" + parameters %>"/>
<bean:define id="cancel" value="<%= "/resultParticipations/selectUnit.do?" + parameters %>"/>
	
<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.createUnit"/></h2>

<fr:edit id="externalUnitBean" name="externalUnitBean" schema="createExternalUnit"  action="<%= path %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= cancel %>"/>
</fr:edit>