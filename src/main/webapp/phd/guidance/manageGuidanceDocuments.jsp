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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
  </tr>
</table>
<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<%--  ### Documents  ### --%>
<logic:empty name="process" property="latestGuidanceDocumentVersions">
	<strong><bean:message  key="label.phd.guidance.documents" bundle="PHD_RESOURCES"/></strong>
	<br/>
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty>

<br />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=prepareUploadGuidanceDocument" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="link.phd.guidance.upload.document" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<logic:notEmpty name="process" property="latestGuidanceDocumentVersions">	
	
	<strong><bean:message  key="label.phd.guidance.documents" bundle="PHD_RESOURCES"/></strong>
	<p>
	<span class="compress">
	<html:link action="/phdIndividualProgramProcess.do?method=downloadGuidanceDocuments" paramId="processId" paramName="processId">
		<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
	</html:link>
	</span>
	</p>

	<fr:view schema="PhdGuidanceDocument.view" name="process" property="latestGuidanceDocumentVersions">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:link 	name="view" link="${downloadUrl}" label="label.view,PHD_RESOURCES"
						order="0" contextRelative="false" hasContext="false" />
			
			<fr:property name="sortBy" value="documentType=asc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<%--  ### End Of Documents  ### --%>
<br/><br/>

<%--  ### End of Operation Area  ### --%>
