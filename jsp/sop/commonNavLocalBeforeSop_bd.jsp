<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p class="invisible">
	<strong>
		&raquo; Gest&atilde;o de Horários
	</strong>
</p>
<ul>
  <li>
  	<html:link page="<%= "/prepararEscolherContexto.do?executionYearName="
  							+ request.getAttribute("executionYearName")
  							+ "&amp;executionPeriodName="
  							+ request.getAttribute("executionPeriodName") %>">
  		<bean:message key="link.schedules.chooseContext"/>
  	</html:link>
  </li>
</ul>

<p class="invisible">
	<strong>
		&raquo; Listagens de Horários
	</strong>
</p>
<ul>
  <li>
  	<html:link page="<%= "/viewAllClassesSchedulesDA.do?method=choose&amp;executionYearName="
  							+ request.getAttribute("executionYearName")
  							+ "&amp;executionPeriodName="
  							+ request.getAttribute("executionPeriodName") %>">
  		<bean:message key="link.schedules.listAllByClass"/>
  	</html:link>
  </li>
  <li>
  	<html:link page="<%= "/viewAllRoomsSchedulesDA.do?method=choose&amp;executionYearName="
  							+ request.getAttribute("executionYearName")
  							+ "&amp;executionPeriodName="
  							+ request.getAttribute("executionPeriodName") %>">
  		<bean:message key="link.schedules.listAllByRoom"/>
  	</html:link>
  </li>
</ul>