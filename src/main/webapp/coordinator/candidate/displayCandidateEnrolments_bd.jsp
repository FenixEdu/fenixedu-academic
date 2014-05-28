<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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