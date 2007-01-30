<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<em><bean:message key="researcher.viewCurriculum.patentsTitle" bundle="RESEARCHER_RESOURCES"/></em> <!-- tobundle -->
	<h2/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.management.title"/></h2>
	
	<ul>
		<li>
			<html:link module="/researcher" page="/resultPatents/prepareCreate.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.create.link"/>
			</html:link>
		</li>
	</ul>
	
	<%-- Action messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Result Patents Listing --%>
	<%--
	<h3 class='cd_heading'><span><bean:message key="researcher.ResearchResultPatent.list.label" bundle="RESEARCHER_RESOURCES"/></span></h3>
	--%>
	
	<logic:empty name="resultPatents">
		<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.emptyList"/></p>
	</logic:empty>

	<logic:notEmpty name="resultPatents">
		<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.management.title"/></strong></p>
		<ul class="nobullet" style="width: 600px;">
		<logic:iterate id="patent" name="resultPatents">
			<bean:define id="patentId" name="patent" property="idInternal"/>
			<li class="mtop1">
				<p class="mvert0">
		 			<strong>
					<a href="<%= request.getContextPath() + "/researcher/resultPatents/showPatent.do?resultId=" + patentId %>">
		 			<fr:view name="patent" property="title"/>
					</a>	 			
		 			</strong>
	 			</p>
	 			<p class="mvert0">
	 			<span style="color: #888;">
		 			<bean:message key="label.registrationDate" bundle="RESEARCHER_RESOURCES"/>
		 			<fr:view name="patent" property="registrationDate"/>
		 		</span>
		 		 - 
	 			<span style="color: #888;">
	 				<bean:message key="label.approvalDate" bundle="RESEARCHER_RESOURCES"/>
		 			<fr:view name="patent" property="approvalDate"/>
		 		</span>
		 		</p>
	 			<logic:equal name="patent" property="note.empty" value="false">
					<p class="mvert0">
						<fr:view name="patent" property="note"/>
					</p>
				</logic:equal>
 			</li>
		</logic:iterate>		
		</ul>
			
	</logic:notEmpty>
	
	
</logic:present>
