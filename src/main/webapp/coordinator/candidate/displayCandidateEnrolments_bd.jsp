<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<logic:present name="candidateEnrolments">
	<bean:message key="label.degree" />:<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/><br />
	<bean:message key="label.masterDegree.administrativeOffice.executionYear" />:<bean:write name="executionDegree" property="infoExecutionYear.year"/><br />
		<strong><bean:message key="label.neededCredits" />: </strong>
		<bean:write name="executionDegree" property="infoDegreeCurricularPlan.neededCredits"/>
   		<br />
   		<br />
		<table>
           	<logic:iterate id="candidateEnrolment" name="candidateEnrolments" >	
	           	<tr>
	           		<td>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourse.name"/>
            		</td>
	           	<%-- <td>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourseScope.infoBranch.name"/>
            		</td> --%>
	           		<td>
            			<bean:write name="candidateEnrolment" property="infoCurricularCourse.credits"/>
					</td>		
				</tr>
	       	</logic:iterate>
	    </table>
   		<br />
   		<br />
   		<strong><bean:message key="label.totalCredits" />: </strong><bean:write name="givenCredits"/>
   		<br />
   		<br />
		<bean:define id="link">
			/displayCourseListToStudyPlan.do?method=print<%= "&" %>candidateID=<bean:write name="candidateID"/>
		</bean:define> 	    
 	    <html:link page='<%= pageContext.findAttribute("link").toString() %>' target="_blank">
	   		<bean:message key="link.masterDegree.printCandidateStudyPlan" />
	    </html:link>
</logic:present>