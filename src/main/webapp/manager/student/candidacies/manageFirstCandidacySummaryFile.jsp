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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%><html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="role(MANAGER)">
	<h2>Gerir Ficheiro de Sumário</h2>

	<fr:hasMessages for="student-number-bean" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<logic:messagesPresent message="true" property="success">
		<div class="success5 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>

	<fr:form action="/candidacySummary.do?method=searchCandidacy">

		<fr:edit id="student-number-bean" name="studentNumberBean" schema="StudentNumberBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			</fr:layout>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit" /></html:submit>
	</fr:form>
	
	<logic:present name="candidacy">
		<bean:define id="candidacyID" name="candidacy" property="externalId" />
		
		<br/><br/>
		<html:link	module="/candidate" action="<%= "/degreeCandidacyManagement.do?method=generateSummaryFile&amp;candidacyID=" + candidacyID%>">
			Gerar PDF de Sumário
		</html:link>
		<br/>
		<logic:present name="hasPDF">
			<html:link	module="/candidate" action="<%= "/degreeCandidacyManagement.do?method=showSummaryFile&amp;candidacyID=" + candidacyID%>">
				Mostrar PDF de Sumário
			</html:link>
		</logic:present>
	</logic:present>
</logic:present>