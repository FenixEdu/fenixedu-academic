<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>

<logic:notEmpty name="participant">
	<fr:view schema="PhdIndividualProgramProcess.view.resume" name="participant" property="individualProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:iterate id="accessType" name="participant" property="accessTypes.types">
	<bean:define id="methodName" >prepare<bean:write name="accessType" property="descriptor"/></bean:define>
	
	<html:link action="<%= "/phdExternalAccess.do?method=" + methodName %>"><bean:write name="accessType" property="localizedName"/></html:link>
	<br/>
</logic:iterate>