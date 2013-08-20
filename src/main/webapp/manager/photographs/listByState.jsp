<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="documents.management.title" bundle="MANAGER_RESOURCES" /></h2>

<logic:present role="MANAGER">

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
							         src="<%=request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoId %>"
	        				         altKey="personPhoto" bundle="IMAGE_RESOURCES"
	        				         title="<%= username + " - " + name %>"
	        	 					 style="border: 1px solid #aaa; padding: 4px; margin: 4px;" />
					</logic:iterate>
                </div>
        </logic:iterate>

</logic:present>

</logic:present>