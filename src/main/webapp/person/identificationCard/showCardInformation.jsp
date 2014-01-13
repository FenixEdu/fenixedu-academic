<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="java.net.URLEncoder"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<html:xhtml/>

<h2><bean:message key="title.person.welcome"/> - <bean:message key="label.identification.card"  bundle="APPLICATION_RESOURCES"/></h2>

<%
	final Person person = (Person) request.getAttribute("person");
	final String instructionKey;
	if (person.hasRole(RoleType.TEACHER)) {
	    instructionKey = "label.identification.card.instructions.teacher.employee";
	} else if (person.hasRole(RoleType.EMPLOYEE)) {
	    instructionKey = "label.identification.card.instructions.teacher.employee";
	} else {
	    instructionKey = "label.identification.card.instructions.student";
	}
	final String institutionAcronym = Unit.getInstitutionAcronym();
	final String institutionName = Unit.getInstitutionName().getContent();
	final String username = person.getIstUsername() == null ? "" : person.getIstUsername();
	final String name = URLEncoder.encode(person.getNickname(), "UTF-8");
	final String mobile = person.getMobile();
	final String phone = URLEncoder.encode(mobile == null || mobile.isEmpty() ? person.getPhone() : mobile, "UTF-8");
	final String email = URLEncoder.encode(person.getInstitutionalEmailAddressValue(), "UTF-8");
	final String sibUrl = "https://portal.sibscartoes.pt/Fichas/index/card/rwcb/id/00042/formtype/rnc/" 
	        + "?userId=" + username
			+ "&userIdType=OU"
			+ "&userName=" + name
			+ "&userPhone=" + phone
			+ "&userEmail=" + email
			+ "&userPhoto=https%3A%2F%2Ffenix.ist.utl.pt%2Fpublico%2FretrievePersonalPhoto.do%3Fmethod%3DretrievePhotographOnPublicSpace%26personId%3D" + person.getExternalId() + "%26contentContextPath_PATH%3D%2Fhomepage%2F" + username + "%2Fapresentacao";
%>

<pre style="font-family: Trebuchet MS, Arial, Helvetica, sans-serif; text-align: justify; width: 80%; white-space: pre-wrap; white-space: -moz-pre-wrap; white-space: -pre-wrap; white-space: -o-pre-wrap; word-wrap: break-word;"
><bean:message bundle="APPLICATION_RESOURCES" key="<%= instructionKey %>" arg0="<%= sibUrl %>" arg1="<%= institutionAcronym %>" arg2="<%= institutionName %>"/></pre>

<%--
<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/identificationCard.do?method=prepare&amp;personOID=<bean:write name="person" property="externalId"/></bean:define>
<html:link href="<%= url %>">
	<bean:message bundle="APPLICATION_RESOURCES" key="label.identification.card.download.template"/>
</html:link>
 --%>
