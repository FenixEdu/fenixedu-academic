<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<logic:present name="siteView">
<bean:define id="studentsComponent" name="siteView" property="component" type="DataBeans.InfoSiteStudents"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>
    <span class="error"><html:errors/></span>
    <table>        
		<tr>
	       <td colspan="2">
			 <h2>
			 	<bean:write name="commonComponent" property="executionCourse.nome" />
				<logic:present name="studentsComponent" property="infoCurricularCourseScope">  		
						&nbsp;-&nbsp;&nbsp;-&nbsp;				
						<bean:define id="ano" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year" />
						<bean:define id="semestre" name="studentsComponent" property="infoCurricularCourseScope.infoCurricularSemester.semester" />					
						<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla" />
						<logic:notEqual name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name" value="">
							<bean:message key="property.curricularCourse.branch" />
							<bean:write name="studentsComponent" property="infoCurricularCourseScope.infoBranch.name"/>&nbsp;
						</logic:notEqual>						
						<bean:message key="label.year" arg0="<%= String.valueOf(ano) %>"/>
						<bean:message key="label.period" arg0="<%= String.valueOf(semestre) %>"/>
			   </logic:present>
	   		</h2>
		   </td>
		</tr> 
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.name" />
		   </td>
		</tr>    		
    	<logic:iterate id="student" name="studentsComponent" property="students"> 
			<tr>
				<td class="listClasses">
					<bean:write name="student" property="number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="student" property="infoPerson.nome"/>
				</td>
			</tr>
    	</logic:iterate>
		
    </table>    
</logic:present>