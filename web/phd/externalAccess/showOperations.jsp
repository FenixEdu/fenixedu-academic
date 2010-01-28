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
	<jsp:include page="processDetails.jsp" />
	<br/>
</logic:notEmpty>

<bean:define id="hash" name="participant" property="accessHashCode" />

<ul>
	<logic:iterate id="accessType" name="participant" property="accessTypes.types">
		<bean:define id="methodName" >prepare<bean:write name="accessType" property="descriptor"/></bean:define>
		
		<li>	
		<html:link action="<%= "/phdExternalAccess.do?method=" + methodName + "&amp;hash=" + hash.toString() %>">
			<bean:write name="accessType" property="localizedName"/>
		</html:link>
		</li>
	</logic:iterate>
</ul>
