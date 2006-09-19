<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.candidacies" /></h2>


<table class="tstyle4">
	<tr>
		<th><bean:message key="net.sourceforge.fenixedu.domain.candidacy.Candidacy.number" /></th>
		<th><bean:message key="net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy.executionDegree.degreeCurricularPlan.degree.name" /></th>
		<th><bean:message key="net.sourceforge.fenixedu.domain.candidacy.Candidacy.activeCandidacySituation.candidacySituationType" /></th>
		<th> </th>
	</tr>
	
<logic:iterate id="candidacy" name="UserView" property="person.candidacies">
	<bean:define id="idInternal" name="candidacy" property="idInternal" />
	<tr>
		<td>
			<bean:write name="candidacy" property="number" />
		</td>
		<td>
			<bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.degree.name" /> - <bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.name" />
		</td>
		<td>
			<bean:message name="candidacy" property="activeCandidacySituation.candidacySituationType.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
		</td>
		<td>
			<logic:equal name="candidacy" property="class.name" value="net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy">
				<html:link action="<%="/degreeCandidacyManagement.do?method=showCandidacyDetails&candidacyID=" + idInternal%>">
					<bean:message key="link.viewCandidacyDetails"/>
				</html:link>
			</logic:equal>
			<logic:equal name="candidacy" property="class.name" value="net.sourceforge.fenixedu.domain.candidacy.DFACandidacy">
				<html:link action="<%="/viewCandidacies.do?method=viewDetail&candidacyID=" + idInternal%>">
					<bean:message key="link.viewCandidacyDetails"/>
				</html:link>
			</logic:equal>
		</td>
	</tr>
</logic:iterate>
</table>