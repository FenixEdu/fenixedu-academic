<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<span class="error"><html:errors/></span>
<br />

<logic:present name="candidateEnrolments">

	<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
	<html:form action="/displayCourseListToStudyPlan.do?method=preparePrint">
		<html:hidden property="candidateID"/>
		<table>
			<tr>
				<td>
					<bean:write name="executionDegree" property="infoDegreeCurricularPlan.neededCredits"/>
				</td>
			</tr>
		
           	<logic:iterate id="candidateEnrolment" name="candidateEnrolments" >	
	           	<tr>
	           		<td>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.credits"/>
					</td>		
				</tr>
	       	</logic:iterate>
			<tr>
				<td>
					<bean:write name="givenCredits"/>
				</td>
			</tr>
	    </table>
		<html:submit value="Imprimir" styleClass="inputbutton" property="ok"/>
	</html:form>
</logic:present>


