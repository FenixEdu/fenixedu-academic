<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
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
