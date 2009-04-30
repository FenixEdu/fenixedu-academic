<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<script language="javascript">
	function set_image_size(imagetag, image) {
		var image_width = image.width;
		var image_height = image.height;
		
		if(image_width > 400 || image_height > 300) {
			var width_ratio = 400 / image_width;
			var height_ratio = 300 / image_height;

			imagetag.width = image_width * Math.min(width_ratio, height_ratio);
			imagetag.height = image_height * Math.min(width_ratio, height_ratio);
		} else {
			imagetag.width = image_width;
			imagetag.height = image_height;
		}
	}
</script>


<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span class="actual"><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> > 
	<span><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><span><bean:message key="message.all.fields.are.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form id="over23CandidacyForm" action='<%= mappingPath + ".do?method=continueCandidacyCreation" %>' encoding="multipart/form-data">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
 		<h2><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean" 
			schema="PublicCandidacyProcess.candidacyDataBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= mappingPath + ".do?method=executeCreateCandidacyPersonalInformationInvalid" %>' />
		</fr:edit>


		<table>
			<tr>
				<td class="width175px">
					<bean:message key="label.photo" bundle="CANDIDATE_RESOURCES"/>:
				</td>
				<td>
					<fr:edit id="individualCandidacyProcessBean.photo"
						name="individualCandidacyProcessBean" 
						schema="PublicCandidacyProcessBean.documentUploadBean.photo"
						property="photoDocument" >
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
					</fr:edit>
				</td>
			</tr>
		</table>
		
							

		<p class="mtop15">
			<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
			<%--<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>--%>
		</p>
</fr:form>
