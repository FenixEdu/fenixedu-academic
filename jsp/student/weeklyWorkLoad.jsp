<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %> 

<h2><bean:message key="link.weekly.work.load"/></h2>

<br/>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
	</html:select>
</html:form>

<bean:size id="s" name="attends"/>
<bean:write name="s"/>

<br/>

<logic:iterate id="attend" name="attends">
	<fr:view name="attend" property="disciplinaExecucao" layout="format">
		<fr:layout>
			<fr:property name="format" value="${nome}"/>
		</fr:layout>
	</fr:view>
	<br/>
	<fr:view name="attend" property="weeklyWorkLoads">
		<fr:layout name="tabular">
			<fr:property name="format" value="${weekOffset}"/>
			<fr:property name="format" value="${contact}"/>
			<fr:property name="format" value="${autonomousStudy}"/>
			<fr:property name="format" value="${other}"/>
		</fr:layout>
	</fr:view>
	<br/>
	<fr:edit name="weeklyWorkLoadBean" action="/weeklyWorkLoad.do?method=create" schema="weekly.work.load.bean">
		<fr:hidden slot="attendsID" name="attend" property="idInternal"/>
	</fr:edit>
	<br/>
	<br/>
</logic:iterate>