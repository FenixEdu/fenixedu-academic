<%@page import="net.sourceforge.fenixedu.util.Month"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />

	<ul>
		<li class="navheader">
			<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.people"/>
		</li>
		<li>
			<html:link action="/findPerson.do?method=prepareFindPerson">
				<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.people.search"/>
			</html:link>
		</li>
<logic:present role="MANAGER">
		<li>
			<html:link action="/personnelManagePeople.do?method=prepareCreatePerson">
				<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.people.create"/>
			</html:link>
		</li>
</logic:present>
	</ul>

<bean:define id="month"
	value="<%=net.sourceforge.fenixedu.util.Month.values()[new YearMonthDay().getMonthOfYear()-1].name()%>" />
<bean:define id="year"
	value="<%=new Integer(new YearMonthDay().getYear()).toString() %>" />

<logic:present name="yearMonth">
	<bean:define id="month" name="yearMonth" property="month.name"
		type="java.lang.String" />
	<bean:define id="yearInteger" name="yearMonth" property="year"
		type="java.lang.Integer" />
	<bean:define id="year" value="<%=yearInteger.toString() %>" />
</logic:present>

<jsp:include page="/commons/functionalities/side-menu.jsp">
	<jsp:param name="month" value="<%=month.toString() %>" />
	<jsp:param name="year" value="<%=year.toString() %>" />
</jsp:include>
