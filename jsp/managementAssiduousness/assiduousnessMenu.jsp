<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%><%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW); %><bean:define id="yearMonthUrl" value="" /><logic:present name="yearMonth"> <% if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) { %>	<bean:define id="month" name="yearMonth" property="month" />	<bean:define id="year" name="yearMonth" property="year"/>	<bean:define id="yearMonthUrl" value="<%="&month="+month.toString()+"&year="+year.toString()%>" />	<%}%></logic:present>	
<ul>
	<li class="navheader"><bean:message key="link.employeeConsult" /></li>
	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showWorkSheet"+yearMonthUrl%>"><bean:message key="link.workSheet" /></html:link></li>
	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showSchedule"+yearMonthUrl%>"><bean:message key="label.schedule" /></html:link></li>
	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showClockings"+yearMonthUrl%>"><bean:message key="link.clockings" /></html:link></li>
	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showJustifications"+yearMonthUrl%>"><bean:message key="link.justifications" /></html:link></li>
<%--  	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showBalanceResume"+yearMonthUrl%>"><bean:message key="link.balanceResume" /></html:link></li>--%>
	<li><html:link page="<%="/viewEmployeeAssiduousness.do?method=chooseEmployee&amp;action=showVacations"+yearMonthUrl%>"><bean:message key="link.vacations" /></html:link></li>
	<% if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) { %>
	<li class="navheader"><bean:message key="link.allEmployee" /></li>
	<li><html:link page="/employeeAssiduousness.do?method=prepareInsertJustification"><bean:message key="link.insertJustification" /></html:link></li>
	<%}%>
	<li class="navheader"><bean:message key="link.consult" /></li>
	<li><html:link page="/viewAssiduousness.do?method=showSchedules"><bean:message key="link.schedules" /></html:link></li>
	<li><html:link page="/viewAssiduousness.do?method=showJustificationMotives"><bean:message key="link.justifications" /></html:link></li>
	<li><html:link page="/viewAssiduousness.do?method=showRegularizationMotives"><bean:message key="link.regularizations" /></html:link></li>	<li class="navheader"><bean:message key="link.export" /></li>
    <li><html:link page="/exportAssiduousness.do?method=chooseYearMonth&amp;action=exportWorkSheets&amp;chooseBetweenDates=true"><bean:message key="link.exportWorkSheets" /></html:link></li>
    <li><html:link page="/exportAssiduousness.do?method=chooseYearMonth&amp;action=exportMonthResume"><bean:message key="link.exportMonthResume" /></html:link></li>
    <li><html:link page="/exportAssiduousness.do?method=chooseYearMonth&amp;action=exportJustifications"><bean:message key="link.exportJustifications" /></html:link></li>
    
    <% if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) { %>
    <li class="navheader"><bean:message key="title.monthClosure" /></li>
    <li><html:link page="/monthClosure.do?method=prepareToCloseMonth"><bean:message key="link.closeMonth" /></html:link></li>
    
    <li class="navheader"><bean:message key="title.extraWork" /></li>
	<li><html:link page="/createExtraWorkAuthorization.do?method=prepareCreateExtraWorkAuthorization"><bean:message key="link.insertAuthorization" /></html:link></li>
	<li><html:link page="/manageExtraWorkAuthorization.do?method=prepareExtraWorkAuthorizationsSearch"><bean:message key="link.showAuthorizations" /></html:link></li>	<li><html:link page="/extraWorkPaymentRequest.do?method=chooseUnitYearMonth"><bean:message key="link.paymentRequests" /></html:link></li>
	<li><html:link page="/manageUnitsExtraWorkAmounts.do?method=chooseYear"><bean:message key="link.unitAmounts" /></html:link></li>
    <li><html:link page="/monthClosure.do?method=prepareToCloseExtraWorkMonth"><bean:message key="link.closeExtraMonth" /></html:link></li>
	<%}%>
</ul>   