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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.coordinator.thesis"/></h2>

<logic:present name="manageThesisContext">
	<div class="infoop8">
		<table>
			<tr>
				<td width="40%">
					<fr:form>
						<fr:edit id="manageThesisContext" name="manageThesisContext" schema="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ManageThesisContext">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
								<fr:property name="columnClasses" value=",,tdclear"/>
							</fr:layout>
						</fr:edit>
					</fr:form>
				</td>
				<td>
					<p class="infoop8">
						<bean:message key="message.coordinator.thesis.introduction.information"/>
					</p>
				</td>
			</tr>
			<tr>
				<td align="center" colspan="2">
					<html:link page="/thesisProcess.do?method=showInformation&amp;degreeCurricularPlanID=">
						<bean:message key="label.coordinator.thesis.periods.and.rules"/>
					</html:link>
					&nbsp;&nbsp;|&nbsp;&nbsp;
					<html:link page="/thesisProcess.do?method=showInformation&amp;degreeCurricularPlanID=">
						<bean:message key="label.coordinator.thesis.proposals"/>
					</html:link>
					&nbsp;&nbsp;|&nbsp;&nbsp;
					<html:link page="/thesisProcess.do?method=showInformation&amp;degreeCurricularPlanID=">
						<bean:message key="label.coordinator.thesis.evaluation.process"/>
					</html:link>
				</td>
			</tr>
		</table>
	</div>
</logic:present>

<logic:notPresent name="manageThesisContext">
	<bean:message key="message.coordinator.thesis.no.execution.degree"/>
</logic:notPresent>