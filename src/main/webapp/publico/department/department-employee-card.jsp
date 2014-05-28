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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<html:xhtml/>

<table class="personInfo mbottom1">
	<tr>
		<td class="personInfo_photo">
			<bean:define id="personId" name="employee" property="person.externalId" type="java.lang.String" />
			<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + personId %>"/></div>		  
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
