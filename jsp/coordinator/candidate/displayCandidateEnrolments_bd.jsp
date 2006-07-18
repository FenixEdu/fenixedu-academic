<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
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