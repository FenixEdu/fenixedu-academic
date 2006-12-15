<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>

<ul>
	<li class="navheader"><bean:message key="title.assiduousness" /></li>
	<li><html:link page="/assiduousnessRecords.do?method=showEmployeeInfo">
		<bean:message key="label.schedule" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showWorkSheet">
		<bean:message key="link.workSheet" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showClockings">
		<bean:message key="link.clockings" />
	</html:link></li>
	<li><html:link
		page="/assiduousnessRecords.do?method=showJustifications">
		<bean:message key="link.justifications" />
	</html:link></li>

	
    <% IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
    if (userView.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.ASSIDUOUSNESS_RESPONSIBLE)) {%>
	<li class="navheader"><bean:message
		key="title.assiduousnessResponsible" /></li>
	<li><html:link
		page="/assiduousnessResponsible.do?method=showEmployeeList">
		<bean:message key="label.employees" />
	</html:link></li>
	<% } %>
</ul>

