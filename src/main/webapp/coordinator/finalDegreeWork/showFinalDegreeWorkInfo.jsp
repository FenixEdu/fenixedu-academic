<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>

<jsp:include page="/coordinator/context.jsp" />

<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>

<p class="mbottom05 mtop05"><strong><bean:message key="title.dissertations"/></strong></p>

<table class="tstyle1 thlight thleft tdright mtop05">
	<logic:iterate id="stateCount" name="summary" property="thesisCount">
		<bean:define id="key" name="stateCount" property="key"/>
		<tr>
			<th>
				<bean:write name="key" property="label"/>
			</th>
			<td>
				<logic:notEqual name="stateCount" property="value" value="0">
					<logic:present name="executionYearId">
						<bean:define id="executionYearId" name="executionYearId"/>
						<html:link page="<%= "/manageThesis.do?method=listThesis&executionYearId=" + executionYearId + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&filter=" + key %>">
						<bean:write name="stateCount" property="value"/>
					</html:link>
				</logic:present>
			</logic:notEqual>
			<logic:equal name="stateCount" property="value" value="0">0</logic:equal>
			<logic:notEqual name="stateCount" property="value" value="0">
				<logic:notPresent name="executionYearId">
					<bean:write name="stateCount" property="value"/>
				</logic:notPresent>
			</logic:notEqual>
		</td>
	</tr>
</logic:iterate>
</table>



</div>
<!-- End Wrap -->
