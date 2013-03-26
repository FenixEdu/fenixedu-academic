<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
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