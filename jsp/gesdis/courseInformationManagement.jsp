<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.courseInformation"/></h2>
<logic:present name="siteView"> 
<html:form action="/courseInformation">
<bean:define id="siteCourseInformation" name="siteView" property="component"/>
<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
<br/>
<table>
<tr>
	<td><bean:message key="message.courseInformation.courseName" /> 
		&nbsp;<bean:write name="executionCourse" property="name" /></td>
	<td></td>
	<td></td>
	<td><bean:message key="message.courseInformation.executionYear" />
		&nbsp;<bean:write name="executionYear" property="year" /></td>
</tr>
<tr>
	<td>
	<logic:iterate id="" name="" property="" type="">
		<bean:message key="message.courseInformation.curricularYear" />
		&nbsp;<bean:write name="courseInformation" property="curricularYear" />
	</logic:iterate>
	</td>
	<td><bean:message key="message.courseInformation.semester" />
		&nbsp;<bean:write name="courseInformation" property="semester" /></td>
	<td><bean:message key="message.courseInformation.courseType" />
		&nbsp;<bean:write name="courseInformation" property="" /></td>
	<td><bean:message key="message.courseInformation.courseSemesterOrAnual" />
		&nbsp;<bean:write name="courseInformation" property="" /></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.responsibleForTheCourse" />
		&nbsp;<bean:write name="courseInformation" property="responsibleForTheCourse" /></td>
	<td></td>
	<td><bean:message key="message.courseInformation.categoryOfTheResponsibleForCourse" />
		&nbsp; <bean:write name="courseInformation" property="categoryOfTheResponsibleForCourse" /></td>
	<td></td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="50px"><span class="emphasis-box">1</span></td>
		<td class="infoop"><bean:message key="message.courseInformation.timeTable" />
		</td>
	</tr>
</table>
<br />
<table width="100%" border=1>
	  <td width="200px"><bean:message key="message.courseInformation.classType"/>
	</td>
	    <td><bean:message key="message.courseInformation.numberOfClasses"/>
	</td>
	    <td><bean:message key="message.courseInformation.classDuration"/>
	</td>
	<td><bean:message key="message.courseInformation.totalDuration"/></td>
</tr>
<tr>
	<td width="200px">
	  <bean:message key="message.courseInformation.typeClassTeoricas"/>
	</td>
	<td>
	<!-- VER ESTES CAMPOS PORQUE NAO SAO PARA SER REESCRITOS, É PARA IR BUSCAR OS VALORES ALGURES-->
	  <bean:write name="typeTeoricas" property="numberOfClassTeoricas" filter="false"/>
	</td>
	<td>
	  <bean:write name="typeTeoricas" property="classDurationTeoricas" filter="false"/>
    </td>
	<td>
	  <bean:write name="typeTeoricas" property="totalDurationTeoricas" filter="false"/>
    </td>
</tr>
<tr>
	<td width="200px">
	  <bean:message key="message.courseInformation.typeClassPraticas"/>
	</td>
	<td>
	<!-- VER ESTES CAMPOS PORQUE NAO SAO PARA SER REESCRITOS, É PARA IR BUSCAR OS VALORES ALGURES-->
	  <bean:write name="typePraticas" property="numberOfClassPraticas" filter="false"/>
	</td>
	<td>
	  <bean:write name="typePraticas" property="classDurationTeoricas" filter="false"/>
    </td>
	<td>
	  <bean:write name="typePraticas" property="totalDurationPraticas" filter="false"/>
    </td>
</tr>
<tr>
	<td width="200px">
	  <bean:message key="message.courseInformation.typeClassTeoPrat"/>
	</td>
	<td>
	<!-- VER ESTES CAMPOS PORQUE NAO SAO PARA SER REESCRITOS, É PARA IR BUSCAR OS VALORES ALGURES-->
	  <bean:write name="typeTeoPrat" property="numberOfClassTeoPrat" filter="false"/>
	</td>
	<td>
	  <bean:write name="typeTeoPrat" property="classDurationTeoPrat" filter="false"/>
    </td>
	<td>
	  <bean:write name="typeTeoPrat" property="totalDurationTeoPrat" filter="false"/>
    </td>
</tr>
<tr>
	<td width="200px">
	  <bean:message key="message.courseInformation.typeClassLab"/>
	</td>
	<td>
	<!-- VER ESTES CAMPOS PORQUE NAO SAO PARA SER REESCRITOS, É PARA IR BUSCAR OS VALORES ALGURES-->
	  <bean:write name="typeLab" property="numberOfClassLab" filter="false"/>
	</td>
	<td>
	  <bean:write name="typeLab" property="classDurationLab" filter="false"/>
    </td>
	<td>
	  <bean:write name="typeLab" property="totalDurationLab" filter="false"/>
    </td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">2</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.LecturingTeachers" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:message key="message.courseInformation.numberOfStudents"/></td>
	<td> <bean:write name="courseInformation" property="numberOfStudents"/></td>
</tr>
</table>
<table border=1>
<tr>
	<td> <bean:message key="message.courseInformation.nameOfTeacher"/></td>
	<td> <bean:message key="message.courseInformation.categoryOfTeacher"/></td>
	<td> <bean:message key="message.courseInformation.typeOfClassOfTeacher"/></td>
</tr>
<logic:iterate id="courseInformation" name="component" property="infoCourseInformation" type="DataBeans.InfoCourseInformation">
<!--<logic:equal name="courseInformation" property="summaryType.siglaTipoAula" value="T"> QUEREMOS A LISTA TODA-->
<!--<bean:define id="hasT" value="whatever"/>-->
<tr>
	<td> <bean:write name="courseInformation" property="nameOfTeacher"/></td>
	<td> <bean:write name="courseInformation" property="categoryOfTeacher"/></td>
	<td> <bean:write name="courseInformation" property="typeOfClassOfTeacher"/></td>
</tr>
<!--</logic:equal>-->
</logic:iterate>
<!--<logic:notPresent name="hasT">-->
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">3</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.CourseResults" />
	</td>
</tr>
</table>
<table border=1>
<tr>
	<td></td>
	<td><bean:message key="message.courseInformation.numberOfStudents" /></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.enrolledStudents" /></td>
	<td><bean:write name="courseInformation" property="enrolledStudents"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.evaluatedStudents" /></td>
	<td><bean:write name="courseInformation" property="evaluatedStudents"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedStudents" /></td>
	<td><bean:write name="courseInformation" property="approvedStudents"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.evaluatedPerEnrolled" /></td>
	<!-- VER ONDE SE FAZEM AS CONTAS-->
	<td><bean:write name="courseInformation" property="evaluatedPerEnrolled"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedPerEvaluated" /></td>
	<td><bean:write name="courseInformation" property="approvedPerEvaluated"/></td>
</tr>
<tr>
	<td><bean:message key="message.courseInformation.approvedPerEnrolled" /></td>
	<td><bean:write name="courseInformation" property="approvedPerEnrolled"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">4</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseObjectives" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:write name="courseInformation" property="courseObjectives"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">5</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseProgram" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:write name="courseInformation" property="courseProgram"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">6</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseBibliographicReference" />
	</td>
</tr>
</table>
<table>
<tr>
	<td><bean:message key="message.courseInformation.coursePrincipalBibliographicReference" /></td>
</tr>
<logic:iterate id="courseInformation" name="component" property="infoCourseInformation" type="DataBeans.InfoCourseInformation">
<tr>
	<td> <bean:write name="courseInformation" property="principalBibliographicReference"/></td>
</tr>
</logic:iterate>
</table>
<table>
<tr>
	<td><bean:message key="message.courseInformation.courseSecondaryBibliographicReference" /></td>
</tr>
<logic:iterate id="courseInformation" name="component" property="infoCourseInformation" type="DataBeans.InfoCourseInformation">
<tr>
	<td> <bean:write name="courseInformation" property="secondaryBibliographicReference"/></td>
</tr>
</logic:iterate>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">7</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseAvaliationMethods" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:write name="courseInformation" property="courseAvaliationMethods"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">8</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseSupportLessons" />
	</td>
</tr>
</table>
<table>
<tr>
	<td> <bean:write name="courseInformation" property="courseSupportLessons"/></td>
</tr>
</table>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop" width="50px"><span class="emphasis-box">9</span></td>
	<td class="infoop">
	  <bean:message key="message.courseInformation.courseReport" />
	</td>
</tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0"  border=1>
<tr>
	<td><html:textarea name="bodyComponent" property="courseReport"/></td> 
</tr>
</table>
<h3>
<table>
<html:hidden property="method" value="editCourseInformation"/>
<html:hidden property="page" value="1"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<tr align="center">	
	<td>
	<html:submit styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>
	</td>
	<td>
		<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
	</td>
</tr>
</table>
</h3>
</html:form>
</logic:present>
