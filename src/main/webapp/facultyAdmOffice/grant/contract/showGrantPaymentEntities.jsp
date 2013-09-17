<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />


<title>Lista de Entidades Pagadoras do IST</title>

<body bgcolor="yellow">

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<strong><p align="center">
	<logic:present name="project">
		<bean:message key="label.grant.paymententity.project.list"/>
	</logic:present>
	<logic:present name="costcenter">
		<bean:message key="label.grant.paymententity.costcenter.list"/>
	</logic:present>
</p></strong><br/>

<logic:present name="grantPaymentList">
<center>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant payment entities description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.paymententity.number"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.paymententity.designation"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.paymententity.teacher"/>
        </th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantPaymentEntity" name="grantPaymentList">
        <tr>
            <td class="listClasses">
                <bean:write name="infoGrantPaymentEntity" property="number"/>
            </td>
            <td class="listClasses">
                <bean:write name="infoGrantPaymentEntity" property="designation"/>
            </td>
            <td class="listClasses">
				<logic:present name="infoGrantPaymentEntity" property="infoResponsibleTeacher">
	                <bean:write name="infoGrantPaymentEntity" property="infoResponsibleTeacher.teacherId"/>
				</logic:present>&nbsp;
	        </td>
        </tr>
    </logic:iterate>
    </table>
</center>
</logic:present>

</logic:messagesNotPresent>

</body>

</html:html>