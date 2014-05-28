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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>


	<table>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.degree" /></strong>
			</td>
			<td>
				<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
			</td>
		</tr>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.executionYear" /></strong>
			</td>
			<td>
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
			</td>
		</tr>
	</table>

	<br />
	<br />


	<bean:define id="link">/candidateRegistration.do?method=prepareCandidateRegistration<%= "&" %>candidateID=
	</bean:define>


    <logic:present name="candidateList">
    	<br />
        <h2><bean:message key="title.masterDegree.administrativeOffice.listCandidates" /></h2>
    	<br />
    	<table>
			<tr>
				<td class="listClasses">
					<bean:message key="label.masterDegree.administrativeOffice.number" />
				</td>
				<td class="listClasses">
					<bean:message key="label.masterDegree.administrativeOffice.name" />
				</td>
				<td class="listClasses">
					<bean:message key="label.specialization" />
				</td>
			</tr>
    	
	    	<logic:iterate id="candidate" name="candidateList" >
        		<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="externalId"/>
    			</bean:define>
    			<tr>
					<td class="listClasses">
						<bean:write name="candidate" property="candidateNumber"/>
					</td>
					<td class="listClasses">
		      			<html:link page="<%= pageContext.findAttribute("candidateLink").toString() %>">
		    				<bean:write name="candidate" property="infoPerson.nome" />
		    			</html:link>
					</td>
					<td class="listClasses">
						<bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
    			</tr>
    		</logic:iterate>
    	</table>
   </logic:present>