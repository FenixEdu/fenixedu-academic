<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="documents.management.title" bundle="MANAGER_RESOURCES" /></h2>

<logic:present name="photographs">

        <logic:iterate id="datedRejection" name="photographs">
                <p class="separator2 mtop2" style="font-size: 1.1em;">
                    <fr:view name="datedRejection" property="date" layout="no-time" />
                </p>
                <div>
	                <logic:iterate id="photo" name="datedRejection" property="photographs">
	                       <bean:define id="photoId" name="photo" property="externalId"/>
                           <bean:define id="username" name="userHistory" property="person.username"/>
                           <bean:define id="name" name="userHistory" property="person.name"/>
	    				   <html:img align="middle"
							         src="${fr:checksum('/person/retrievePersonalPhoto.do?method=retrievePendingByID&photoCode='.concat(photoId))}"
	        				         altKey="personPhoto" bundle="IMAGE_RESOURCES"
	        				         title="<%= username + " - " + name %>"
	        	 					 style="border: 1px solid #aaa; padding: 4px; margin: 4px;" />
					</logic:iterate>
                </div>
        </logic:iterate>

</logic:present>