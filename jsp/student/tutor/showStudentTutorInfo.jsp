<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.student.tutorship.operations" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.student.tutorship.tutorshipInfoTitle" bundle="APPLICATION_RESOURCES"/></h2>

<!-- AVISOS E ERROS -->
<span class="error0">
<html:errors />
</span>
<br />

<ul>
	<li>
		<html:link href="http://gep.ist.utl.pt/html/tutorado"
			target="_blank">
			<bean:message key="link.teacher.tutorship.gepTutorshipPage" />
		</html:link>
	</li>
</ul>

<!-- ACTUAL TUTOR INFORMATION -->
<logic:present name="actualTutor">
	<logic:notEmpty name="actualTutor" >
		<h3><bean:message key="label.student.tutorship.actualTutorInfo" bundle="APPLICATION_RESOURCES"/></h3>
		<%-- Foto --%>
		<div style="float: right;" class="printhidden">
		<bean:define id="personId" name="personID" />
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personId.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
		</div>
		<fr:view name="actualTutor" layout="tabular" schema="student.tutorship.tutorInfo" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright"/>
				<fr:property name="rowClasses" value="bold,,,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="actualTutor" >
	<span class="error0"><b><bean:message key="label.student.tutorship.noActualTutor" bundle="APPLICATION_RESOURCES"/></b></span>
</logic:notPresent>

<br />

<!-- PAST TUTORS INFORMATION -->
<logic:present name="pastTutors">
	<logic:notEmpty name="pastTutors" >
	<h3><bean:message key="label.student.tutorship.pastTutorsInfo" bundle="APPLICATION_RESOURCES"/></h3>
		<logic:iterate id="pastTutorsId" name="pastTutors">
			<fr:view name="pastTutorsId" layout="tabular" schema="student.tutorship.tutorInfo" >
				<fr:layout>
					<fr:property name="classes" value="tstyle2 thlight thright"/>
				</fr:layout>
			</fr:view>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>
<br />

<!-- STUDENTS STATISTICS -->
<logic:present name="studentStatistics">
	<logic:notEmpty name="studentStatistics" >
	<h3><bean:message key="label.student.statistics.table" bundle="APPLICATION_RESOURCES"/></h3>
			<fr:view name="studentStatistics" layout="tabular-sortable" schema="student.statistics.table" >
				<%-- <fr:layout>
					<fr:property name="classes" value="tstyle2 thlight thright"/>
				</fr:layout>--%>
			<fr:layout>
				<fr:property name="classes" value="tstyle1 mtop1 tdcenter"/>
				<fr:property name="columnClasses" value="acenter,acenter,acenter,acenter,acenter"/>
				<fr:property name="suffixes" value="º sem,,,%, "/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="executionPeriod,totalEnrolmentsNumber,approvedEnrolmentsNumber,approvedRatio,aritmeticAverage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/viewTutorInfo.do?method=prepare") %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "executionPeriod" : request.getParameter("sortBy") %>"/>
			</fr:layout>
			</fr:view>
	</logic:notEmpty>
</logic:present>
<br />

<tr>
	<td>
		<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
		<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
		<bean:define id="graph" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/student/viewTutorInfo.do?method=createAreaXYChart</bean:define>
		<html:img align="middle" src="<%= graph %>" altKey="" bundle="IMAGE_RESOURCES"/>
	</td>
</tr>