<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.management.title"/></h2>

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


<logic:empty name="resultPatents">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.emptyList"/></em></p>
</logic:empty>

<logic:notEmpty name="resultPatents">
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.patentList"/></strong></p>
	<div>
	<ul class="listresearch">
	<logic:iterate id="patent" name="resultPatents">
		<bean:define id="patentId" name="patent" property="externalId"/>
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
	</div>
		
</logic:notEmpty>
