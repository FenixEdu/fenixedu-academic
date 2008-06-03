<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<div style="border-top: 1px solid #ccc; padding: 0.75em 0; margin: 0.5em 0;">
	<table class="tdtop width100">
		<tr>
			<td style="width: 100px;">
			<logic:equal name="researcher" property="person.availablePhoto" value="true">
				<bean:define id="personId" name="researcher" property="person.idInternal"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode=" + personId%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
			</logic:equal>
			<logic:equal name="researcher" property="person.availablePhoto" value="false">
				<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
				<img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/>
			</logic:equal>
			</td> 
			<td>
				<p class="mtop0 mbottom05"><strong><fr:view name="researcher" property="person" layout="values" schema="person.name.with.link"/></strong></p>
				<p class="mtop0 mbottom05"><fr:view name="researcher" property="person.employee.currentDepartmentWorkingPlace.realName"/></p>
				<p class="mtop0 mbottom05"><bean:message key="label.allows.contact.from" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="researcher" layout="researcher-allowed-contacts"/></p>
				<logic:present name="researcher" property="person.personWorkPhone">
					<p class="mtop0 mbottom05"><bean:message key="label.person.telephone" bundle="APPLICATION_RESOURCES"/> <fr:view name="researcher" property="person.workPhone"/></p>
				</logic:present>
				<logic:present name="researcher" property="person.defaultEmailAddress">			
					<logic:equal name="researcher" property="person.defaultEmailAddress.visible" value="true">
							<p class="mtop0 mbottom05"><bean:message key="label.email" bundle="APPLICATION_RESOURCES"/>: <a href="mailto:<fr:view name="researcher" property="person.defaultEmailAddress.value"/>"><fr:view name="researcher" property="person.defaultEmailAddress.value"/></a></p>
					</logic:equal>
				</logic:present>
				
				<p class="mtop0 mbottom05"><bean:message key="researcher.interests.title" bundle="RESEARCHER_RESOURCES"/>: 
				<logic:notEmpty name="researcher"  property="researchInterests">
					<fr:view name="researcher"  property="researchInterests" layout="values-comma" schema="researchInterest.name"/>
				</logic:notEmpty>
				<logic:empty name="researcher"  property="researchInterests">
					-
				</logic:empty>
				</p>
				<p class="mtop0 mbottom05"><bean:message key="label.keywords" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="researcher" property="keywords"/></p>
			</td>
		</tr>	
	</table>
</div>