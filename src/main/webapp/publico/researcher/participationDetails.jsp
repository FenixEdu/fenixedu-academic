<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<bean:define id="activityId" name="researchActivity" property="idInternal"/>
	<bean:define id="parameter" value="<%= "activityId=" + activityId %>"/>
	<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
	<bean:define id="schema" value="<%= activityType + ".view-defaults" %>" type="java.lang.String" scope="request" />
	
	<h1><bean:message key="<%= "label." + activityType%>" bundle="RESEARCHER_RESOURCES"/></h1>
			
	<%-- DATA --%>		
	<fr:view name="researchActivity" schema="<%= schema %>">
	    <fr:layout name="tabular-nonNullValues">
    	    <fr:property name="classes" value="tstyle2 thlight thtop thleft"/>
    	    <fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
			<fr:property name="columnClasses" value="width12em, width50em"/>
			<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	    </fr:layout>
	</fr:view>

