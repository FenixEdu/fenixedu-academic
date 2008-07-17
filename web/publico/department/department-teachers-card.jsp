<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<table class="personInfo mbottom1">
	<tr>
		<td class="personInfo_photo">
          	<logic:notEqual name="teacher" property="person.photoPubliclyAvailable" value="true">
          		<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
				<div><img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/></div>
          	</logic:notEqual>

          	<logic:equal name="teacher" property="person.photoPubliclyAvailable" value="true">
      			<bean:define id="homepageID" name="teacher" property="person.homepage.idInternal"/>
      			<div><html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>"/></div>
   			</logic:equal>
		</td>
		<td class="personInfo_info">
			<p class="mtop05 mbottom05" style="font-size: 1.1em;">
				<logic:equal name="teacher" property="person.homePageAvailable" value="true">					
					<app:contentLink name="teacher" property="person.homepage" scope="request">
						<fr:view name="teacher" property="person.nickname" layout="person-name"/>				
					</app:contentLink>							
				</logic:equal>
				<logic:notEqual name="teacher" property="person.homePageAvailable" value="true">
					<fr:view name="teacher" property="person.nickname" layout="person-name"/>
				</logic:notEqual>
			</p>
			
			<logic:present name="byCategory">
				<logic:notEmpty name="teacher" property="currentSectionOrScientificArea">
					<p class="mtop025 mbottom05">
						<fr:view name="teacher" property="currentSectionOrScientificArea.nameI18n"/>
					</p>
				</logic:notEmpty>
			</logic:present>
			<logic:present name="byArea">
				<logic:notEmpty name="teacher" property="category">
					<p class="mtop025 mbottom05">
						<fr:view name="teacher" property="category.name"/>
					</p>
				</logic:notEmpty>
			</logic:present>
			
			<logic:notEmpty name="teacher" property="person.researchInterests">
				<p class="mtop025 mbottom05">
					<bean:message key="label.teacher.scientificInterests" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>: 
					<fr:view name="teacher" property="person.researchInterests" sortBy="interestOrder">
						<fr:layout name="flowLayout">
							<fr:property name="classes" value="color888"/>
							<fr:property name="eachLayout" value="values"/>
							<fr:property name="eachSchema" value="researchInterest.title"/>
							<fr:property name="htmlSeparator" value=", "/>
							<fr:property name="indented" value="false"/>
						</fr:layout>
					</fr:view>
				</p>
			</logic:notEmpty>

            <table class="personInfo2 mvert0 thlight thtop">
                <fr:view name="teacher" property="person.emailAddresses">
                    <fr:layout name="contact-table">
                        <fr:property name="publicSpace" value="true"/>
                        <fr:property name="bundle" value="PUBLIC_DEPARTMENT_RESOURCES" />
                        <fr:property name="label" value="label.teacher.email" />
                    </fr:layout>
                </fr:view>
            </table>

            <table class="personInfo2 mvert0 thlight thtop">
                <fr:view name="teacher" property="person.phones">
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
