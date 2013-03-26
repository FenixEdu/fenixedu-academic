<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>

<br/>
<h2><bean:message key="label.phd.process" bundle="PHD_RESOURCES" /></h2>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="participant">
	<jsp:include page="processDetails.jsp" />
	<br/>
</logic:notEmpty>

<bean:define id="hash" name="participant" property="accessHashCode" />
<bean:define id="mainProcess" name="participant" property="individualProcess" type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" />

<ul>
	<logic:iterate id="accessType" name="participant" property="accessTypes.types" type="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType">
		<bean:define id="methodName" >prepare<bean:write name="accessType" property="descriptor"/></bean:define>
		
		<phd:accessTypeAvailable mainProcess="<%= mainProcess %>" accessType="<%= accessType %>">
			<li>
			<html:link action="<%= "/phdExternalAccess.do?method=" + methodName + "&amp;hash=" + hash.toString() %>">
				<bean:write name="accessType" property="localizedName"/>
			</html:link>
			</li>
		</phd:accessTypeAvailable>
	</logic:iterate>
</ul>
