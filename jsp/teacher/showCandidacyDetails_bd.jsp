<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

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
				<h2><bean:message key="label.viewCandidacyTitle"/></h2>
				<h3><bean:message key="label.seminaries.showCandidacy.Student"/></h3>	
				<table width="90%" class="infotable">
					<tr>
						<td width="75px"><strong><bean:message key="label.seminaries.showCandidacy.Student.Number"/>:</strong></td>
						<td><bean:write name="student" property="number"/></td>
					</tr>
					<tr>
						<td width="75px"><strong><bean:message key="label.seminaries.showCandidacy.Student.Name"/>:</strong></td>
						<td><bean:write name="student" property="infoPerson.nome"/></td>
					</tr>
				</table>
				<h3><bean:message key="label.seminaries.showCandidacy.CurricularCourse"/></h3>
				<table width="90%" class="infotable">
					<tr>
						<td width="75px"><strong><bean:message key="label.seminaries.showCandidacy.CurricularCourse.Name"/>:</Strong></td>
						<td><bean:write name="curricularCourse" property="name"/></td>
					</tr>
					<tr>
						<td width="75px"><strong><bean:message key="label.seminaries.showCandidacy.CurricularCourse.Code"/>:</strong></td>
						<td><bean:write name="curricularCourse" property="code"/></td>
					</tr>
				</table>
				<h3><bean:message key="label.seminaries.showCandidacy.Candidacy"/></h3>
				<table width="90%"class="infotable">
					<logic:present name="theme">
						<logic:notEmpty name="theme">
							<tr>
								<td><strong><bean:message key="label.seminaries.showCandidacy.Theme"/>:</strong></td>
								<td><bean:write name="theme" property="name"/></td>
							</tr>
						</logic:notEmpty>
					</logic:present>
					<tr>
						<td><strong><bean:message key="label.seminaries.showCandidacy.Modality"/>:</strong></td>
						<td><bean:write name="modality" property="name"/></td>
					</tr>
					<tr>
						<td><strong><bean:message key="label.seminaries.showCandidacy.Motivation"/>:</strong></td>
						<td><bean:write name="motivation"/></td>
					</tr>
					<logic:iterate indexId="index" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy" name="cases" id="caseStudy">
					<tr>
						<td><strong><bean:message key="label.seminaries.showCandidacy.Case"/> <%=index.intValue()+1 %></strong></td>
						<td><bean:write name="caseStudy" property="code"/> - <bean:write name="caseStudy" property="name"/></td>
					</tr>
					</logic:iterate>
				</table>
				<br />
				<html:link page="/showCandidacies.do?seminaryID=5"><bean:message key="label.seminaries.showCandidacy.Back"/></html:link><br/>				
					</logic:present>
				</logic:present>
			</logic:present>
	</logic:present>
</logic:present>