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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<bean:define id="cases" type="java.util.List" name="cases"/>
<bean:define id="student" type="net.sourceforge.fenixedu.dataTransferObject.InfoStudent"  name="student"/>
<bean:define id="curricularCourse" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse"  name="curricularCourse"/>
<bean:define id="motivation" type="java.lang.String"  name="motivation"/>
<bean:define id="seminary" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary"  name="seminary"/>
<bean:define id="modality" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality"  name="modality"/>

<logic:present name="cases">
	<logic:present name="student">
		<logic:present name="curricularCourse">
			<logic:present name="curricularCourse">
					<logic:present name="motivation">
				
				<em><bean:message key="label.portal.seminaries"/></em>
				<h2><bean:message key="label.viewCandidacyTitle"/></h2>

				<h3 class="mbottom05"><bean:message key="label.seminaries.showCandidacy.Student"/></h3>	
				<table class="tstyle4 mtop05">
					<tr>
						<td width="75px"><bean:message key="label.seminaries.showCandidacy.Student.Number"/>:</td>
						<td><bean:write name="student" property="number"/></td>
					</tr>
					<tr>
						<td width="75px"><bean:message key="label.seminaries.showCandidacy.Student.Name"/>:</td>
						<td><bean:write name="student" property="infoPerson.nome"/></td>
					</tr>
				</table>
				
				<h3 class="mbottom05"><bean:message key="label.seminaries.showCandidacy.CurricularCourse"/></h3>
				<table class="tstyle4 mtop05">
					<tr>
						<td width="75px"><bean:message key="label.seminaries.showCandidacy.CurricularCourse.Name"/>:</Strong></td>
						<td><bean:write name="curricularCourse" property="name"/></td>
					</tr>
					<tr>
						<td width="75px"><bean:message key="label.seminaries.showCandidacy.CurricularCourse.Code"/>:</td>
						<td><bean:write name="curricularCourse" property="code"/></td>
					</tr>
				</table>
				
				<h3 class="mbottom05"><bean:message key="label.seminaries.showCandidacy.Candidacy"/></h3>
				<table class="tstyle4 mtop05">
					<logic:present name="theme">
						<logic:notEmpty name="theme">
							<tr>
								<td><bean:message key="label.seminaries.showCandidacy.Theme"/>:</td>
								<td><bean:write name="theme" property="name"/></td>
							</tr>
						</logic:notEmpty>
					</logic:present>
					<tr>
						<td><bean:message key="label.seminaries.showCandidacy.Modality"/>:</td>
						<td><bean:write name="modality" property="name"/></td>
					</tr>
					<tr>
						<td><bean:message key="label.seminaries.showCandidacy.Motivation"/>:</td>
						<td><bean:write name="motivation"/></td>
					</tr>
					<logic:iterate indexId="index" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy" name="cases" id="caseStudy">
					<tr>
						<td><bean:message key="label.seminaries.showCandidacy.Case"/> <%=index.intValue()+1 %></td>
						<td><bean:write name="caseStudy" property="code"/> - <bean:write name="caseStudy" property="name"/></td>
					</tr>
					</logic:iterate>
				</table>

				<p>
					<html:link page="/showCandidacies.do?seminaryID=5">&lt; <bean:message key="label.seminaries.showCandidacy.Back"/></html:link>
				</p>
				
					</logic:present>
				</logic:present>
			</logic:present>
	</logic:present>
</logic:present>