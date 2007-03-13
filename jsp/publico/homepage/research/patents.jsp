<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<h1><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></h1>

	<logic:empty name="patents">
		<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.emptyList"/></p>
	</logic:empty>

	<logic:notEmpty name="patents">
		<ul style="width: 600px;">
		<logic:iterate id="patent" name="patents">
			<bean:define id="patentId" name="patent" property="idInternal"/>
			<li>
				<p class="mvert0">
		 			<strong>
					<a href="<%= request.getContextPath() + "/publico/showResearchResult.do?method=showPatent&amp;resulIdt=" + patentId + "&amp;homepageID=" + request.getParameter("homepageID")%>">
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
				<p class="mtop025">
					<logic:iterate id="file" name="patent" property="resultDocumentFiles">
									<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
					</logic:iterate>
				</p>
 			</li>
		</logic:iterate>		
		</ul>
			
	</logic:notEmpty>