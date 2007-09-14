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
					<html:link page="/listAllSeminaries.do"><bean:message key="label.seminaries.showCandidacy.Back"/></html:link>
				</p>
					</logic:present>
				</logic:present>
			</logic:present>
	</logic:present>
</logic:present>