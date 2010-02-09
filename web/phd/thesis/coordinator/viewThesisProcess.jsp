<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadProvisionalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadFinalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadThesisRequirement"%>

<logic:notEmpty name="process" property="thesisProcess">
<logic:equal name="process" property="activeState.active" value="true">

<bean:define id="thesisProcess" name="process" property="thesisProcess" />

<br/>
<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
	<fr:view schema="PhdThesisProcess.view" name="process" property="thesisProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	</td>
	<td>
		<ul class="operations" >
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadProvisionalThesisDocument.class %>">
			<li>
				<bean:define id="provisionalThesisDownloadUrl" name="thesisProcess" property="provisionalThesisDocument.downloadUrl" />
				<a href="<%= provisionalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="provisionalThesisDocument.documentType.localizedName"/>
					(<bean:message  key="label.version" bundle="PHD_RESOURCES"/> <bean:write name="thesisProcess" property="provisionalThesisDocument.documentVersion"/>)
				</a>
			</li>
			</phd:activityAvailable>
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadFinalThesisDocument.class %>">
			<li>
				<bean:define id="finalThesisDownloadUrl" name="thesisProcess" property="finalThesisDocument.downloadUrl" />
				<a href="<%= finalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="finalThesisDocument.documentType.localizedName"/> 
					(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="thesisProcess" property="finalThesisDocument.documentVersion"/>)
				</a>
			</li>
			</phd:activityAvailable>
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadThesisRequirement.class %>">
			<li>
				<bean:define id="thesisRequirementDownloadUrl" name="thesisProcess" property="thesisRequirementDocument.downloadUrl" />
				<a href="<%= thesisRequirementDownloadUrl.toString() %>"><bean:write name="thesisProcess" property="thesisRequirementDocument.documentType.localizedName"/> </a>
			</li>
			</phd:activityAvailable>
			<logic:notEmpty name="thesisProcess" property="juryPresidentDocument">
				<li>
				<bean:define id="url2" name="thesisProcess" property="juryPresidentDocument.downloadUrl" />
				<a href="<%= url2.toString() %>">
					<bean:write name="process" property="juryPresidentDocument.documentType.localizedName"/> 
					(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryPresidentDocument.documentVersion"/>)
				</a>
				</li>
			</logic:notEmpty>
		</ul>
	</td>
  </tr>
 </table>

<ul class="operations">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareSubmitJuryElementsDocument" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.elements.document"/>
		</html:link>
	</li>
	<%-- 
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=manageThesisJuryElements" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.elements"/>
		</html:link>
	</li>
	--%>
</ul>
</logic:equal>
<br/>
</logic:notEmpty>
