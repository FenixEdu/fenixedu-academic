<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<ul>
<%-- <li><bean:message key="link.home"/></li> --%>
<br />
<br />
<%--<li><bean:message key="link.degreeManagement"/> <i>(brevemente)</i></li>--%>
<li><html:link page="/curricularCourseManagement.do" ><bean:message key="link.curricularCourseManagement"/></html:link></li>

</ul>