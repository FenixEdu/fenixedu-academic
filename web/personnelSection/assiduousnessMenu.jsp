<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="net.sourceforge.fenixedu.util.Month"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
