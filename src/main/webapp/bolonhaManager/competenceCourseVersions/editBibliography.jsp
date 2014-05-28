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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="competecenceCourseID" name="bean" property="competenceCourse.externalId"/>

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.competenceCourse.createVersion" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages" /></span>
		</html:messages>
	</p>
</logic:messagesPresent>


	<p class="mbottom05"><strong><bean:message key="label.newBibliographicEntry" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
	<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=createBibliographicReference" %>">
	<fr:edit id="createReference" name="referenceBean" schema="create.reference.from.bean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
		<fr:edit name="bean" id="editVersion" visible="false"/>
		<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
		<html:submit><bean:message key="label.insert" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
	
	<logic:notEmpty name="bean" property="references">
	<p class="mtop2 mbottom05"><strong><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
	<logic:notEmpty name="bean" property="references.mainBibliographicReferences">
	<logic:iterate id="reference" name="bean" property="references.mainBibliographicReferences">
		<bean:define id="index" name="reference" property="order"/>	
		<a name="<%= "biblio" + index %>"></a>
		
		<logic:notPresent name="edit">
			<div class="mbottom15">
				<fr:view name="reference" schema="view.reference">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thlight thright thtop mtop05"/>
					</fr:layout>
				</fr:view>
				<div class="dinline forminline">	
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=editBibliography&index=" + index + "#biblio" + index%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=removeBibliography&index=" + index %>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				</div>			
			</div>
		</logic:notPresent>
		
		<logic:present name="edit">
			<bean:define id="edit" name="edit"/>
		
			<logic:equal name="index" value="<%= edit.toString() %>">
				<div class="dinline forminline">	
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=viewBibliography"%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<fr:edit name="reference" schema="edit.reference">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thlight thright"/>
							<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
						</fr:layout>
					</fr:edit>
					<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>		
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=viewBibliography"%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				</div>
			</logic:equal>
			
			<logic:notEqual name="index" value="<%= edit.toString() %>">
				<div class="mbottom15">
					<fr:view name="reference" schema="view.reference">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thlight thright thtop"/>
						</fr:layout>
					</fr:view>					
					<div class="dinline forminline">	
					<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=editBibliography&index=" + index + "#biblio" + index%>">
						<fr:edit name="bean" id="editVersion" visible="false"/>
						<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
						<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
					</fr:form>
					<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=removeBibliography&index=" + index %>">
						<fr:edit name="bean" id="editVersion" visible="false"/>
						<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
						<html:submit><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></html:submit>
					</fr:form>
					</div>
				</div>
			</logic:notEqual>
		</logic:present>


	</logic:iterate>
	</logic:notEmpty>

	<logic:empty name="bean" property="references.mainBibliographicReferences">
		<p><em><bean:message key="label.noPrimaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></em></p>
	</logic:empty>
	

	<p class="mtop2 mbottom05"><strong><bean:message key="label.secondaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
	<logic:notEmpty name="bean" property="references.secondaryBibliographicReferences">
	<logic:iterate id="secondaryReference" name="bean" property="references.secondaryBibliographicReferences">
		<bean:define id="index" name="secondaryReference" property="order"/>	
		<a name="<%= "biblio" + index %>"></a>
		
		<logic:notPresent name="edit">
			<fr:view name="secondaryReference" schema="view.reference">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thtop"/>
				</fr:layout>
			</fr:view>
			<div class="dinline forminline">	
			<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=editBibliography&index=" + index + "#biblio" + index%>">
				<fr:edit name="bean" id="editVersion" visible="false"/>
				<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
				<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
			</fr:form>
			<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=removeBibliography&index=" + index %>">
				<fr:edit name="bean" id="editVersion" visible="false"/>
				<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
				<html:submit><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></html:submit>
			</fr:form>
			</div>			
		</logic:notPresent>
		
		<logic:present name="edit">
			<bean:define id="edit" name="edit"/>
		
			<logic:equal name="index" value="<%= edit.toString() %>">
				<div class="dinline forminline">	
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=viewBibliography"%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<fr:edit name="secondaryReference" schema="edit.reference">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight"/>
						</fr:layout>
					</fr:edit>
					<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>		
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=viewBibliography"%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				</div>
			</logic:equal>
			
			<logic:notEqual name="index" value="<%= edit.toString() %>">
				<fr:view name="secondaryReference" schema="view.reference"> 
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thlight thright thtop"/>
					</fr:layout>
				</fr:view>					
				<div class="dinline forminline">	
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=editBibliography&index=" + index + "#biblio" + index%>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=removeBibliography&index=" + index %>">
					<fr:edit name="bean" id="editVersion" visible="false"/>
					<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
					<html:submit><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></html:submit>
				</fr:form>
				</div>
			</logic:notEqual>
		</logic:present>


	</logic:iterate>
	</logic:notEmpty>

	<logic:empty name="bean" property="references.secondaryBibliographicReferences">
		<p class="mtop05"><em><bean:message key="label.noSecundaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></em></p>
	</logic:empty>
	
	</logic:notEmpty>
	
<div class="mtop2">
	<div class="dinline forminline">	
		<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=createVersion"%>">
			<fr:edit name="bean" id="editVersion" visible="false"/>
			<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
			<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
		<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competecenceCourseID + "&method=prepareCreateVersion"%>">
			<fr:edit name="bean" id="editVersion" visible="false"/>
			<fr:edit name="beanLoad" id="editVersionLoad" visible="false"/>
			<html:submit><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</div>
</div>