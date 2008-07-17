<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<html:xhtml/>

<table class="personInfo mbottom1">
	<tr>
		<td class="personInfo_photo">
          	<logic:notEqual name="employee" property="person.photoPubliclyAvailable" value="true">
          		<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
				<div><img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/></div>
          	</logic:notEqual>

          	<logic:equal name="employee" property="person.photoPubliclyAvailable" value="true">
      			<bean:define id="homepageID" name="employee" property="person.homepage.idInternal"/>
      			<div><img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>"/></div>
   			</logic:equal>
		</td>
		<td class="personInfo_info">
			<p class="mtop05 mbottom05" style="font-size: 1.1em;">
				<logic:equal name="employee" property="person.homePageAvailable" value="true">									
					<app:contentLink name="employee" property="person.homepage" scope="request">
						<fr:view name="employee" property="person.nickname" layout="person-name"/>					
					</app:contentLink>									
				</logic:equal>
				<logic:notEqual name="employee" property="person.homePageAvailable" value="true">
					<fr:view name="employee" property="person.nickname" layout="person-name"/>
				</logic:notEqual>
			</p>

			<logic:notEmpty  name="employee" property="person.personFunctions">
			<bean:message key="label.functions" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>: 
			<fr:view name="employee" property="person.personFunctions" schema="personFunction.name">
				<fr:layout name="values">
						<fr:property name="htmlSeparator" value=", "/>
				</fr:layout>
			</fr:view>
			</logic:notEmpty>

            <table class="personInfo2 mvert0 thlight thtop">
                <fr:view name="employee" property="person.emailAddresses">
                    <fr:layout name="contact-table">
                        <fr:property name="publicSpace" value="true"/>
                        <fr:property name="bundle" value="PUBLIC_DEPARTMENT_RESOURCES" />
                        <fr:property name="label" value="label.teacher.email" />
                    </fr:layout>
                </fr:view>
            </table>

            <table class="personInfo2 mvert0 thlight thtop">
                <fr:view name="employee" property="person.phones">
                    <fr:layout name="contact-table">
                        <fr:property name="publicSpace" value="true"/>
                        <fr:property name="bundle" value="PUBLIC_DEPARTMENT_RESOURCES" />
                        <fr:property name="label" value="label.teacher.workPhone" />
                        <fr:property name="types" value="WORK" />
                    </fr:layout>
                </fr:view>
            </table>
		</td>
	</tr>
</table>
