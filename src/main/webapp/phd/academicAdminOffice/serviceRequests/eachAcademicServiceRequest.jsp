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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

                <fr:view name="requests" schema="AcademicServiceRequest.view">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle4 thlight mtop0" />
                        <fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,,acenter nowrap,nowrap" />
						
						<fr:link name="view" link="/phdAcademicServiceRequestManagement.do?method=viewAcademicServiceRequest&amp;phdAcademicServiceRequestId=${externalId}" label="view,APPLICATION_RESOURCES" />
						
                        <fr:property name="linkFormat(reject)" value="/phdAcademicServiceRequestManagement.do?method=prepareReject&amp;phdAcademicServiceRequestId=${externalId}"/>
                        <fr:property name="key(reject)" value="reject"/>
                        <fr:property name="visibleIf(reject)" value="rejectedSituationAccepted" />
                        <fr:property name="visibleIfNot(reject)" value="piggyBackedOnRegistry" />

                        <fr:property name="linkFormat(cancel)" value="/phdAcademicServiceRequestManagement.do?method=prepareCancel&amp;phdAcademicServiceRequestId=${externalId}"/>
                        <fr:property name="key(cancel)" value="cancel"/>
                        <fr:property name="visibleIf(cancel)" value="loggedPersonCanCancel"/>
                        <fr:property name="visibleIfNot(cancel)" value="piggyBackedOnRegistry"/>
						
						<fr:link name="payments" label="payments,APPLICATION_RESOURCES" link="<%="/payments.do?method=showOperations" + "&personId=${process.person.externalId}" %>" condition="!isPayed" />
						<fr:property name="visibleIf(payments)" value="paymentsAccessible" />
						
                        <fr:property name="linkFormat(processing)" value="/phdAcademicServiceRequestManagement.do?method=prepareProcess&amp;phdAcademicServiceRequestId=${externalId}"/>
                        <fr:property name="key(processing)" value="processing"/>
                        <fr:property name="visibleIf(processing)" value="processingSituationAccepted"/>
                        <fr:property name="visibleIfNot(processing)" value="piggyBackedOnRegistry"/>

                        <fr:property name="linkFormat(send)" value="/phdAcademicServiceRequestManagement.do?method=prepareSend&amp;phdAcademicServiceRequestId=${externalId}"/>
                        <fr:property name="key(send)" value="label.send"/>
                        <fr:property name="visibleIf(send)" value="sendToExternalEntitySituationAccepted"/>
                        <fr:property name="visibleIfNot(send)" value="managedWithRectorateSubmissionBatch"/>

						<fr:link name="receiveFrom" condition="receivedSituationAccepted" link="/phdAcademicServiceRequestManagement.do?method=prepareReceive&amp;phdAcademicServiceRequestId=${externalId}" label="label.receiveFrom,APPLICATION_RESOURCES"/>
						
						<fr:link name="print" label="print,APPLICATION_RESOURCES" link="/phdAcademicServiceRequestManagement.do?method=downloadLastGeneratedDocument&amp;phdAcademicServiceRequestId=${externalId}" condition="downloadPossible" />
						
						<fr:link name="deliver" condition="deliveredSituationAccepted" label="deliver,APPLICATION_RESOURCES" link="/phdAcademicServiceRequestManagement.do?method=prepareDeliver&amp;phdAcademicServiceRequestId=${externalId}" />
						
						<fr:link name="conclude" condition="concludedSituationAccepted" label="conclude,APPLICATION_RESOURCES" link="/phdAcademicServiceRequestManagement.do?method=prepareConclude&amp;phdAcademicServiceRequestId=${externalId}" />

						<fr:link name="viewBatch" condition="batchSet" label="viewBatch,APPLICATION_RESOURCES" link="/rectorateDocumentSubmission.do?method=viewBatch&batchOid=${rectorateSubmissionBatch.externalId}" target="blank" /> 

    					<fr:property name="order(view)" value="1" />
                        <fr:property name="order(reject)" value="2" />
                        <fr:property name="order(cancel)" value="3" />
                        <fr:property name="order(payments)" value="4" />
                        <fr:property name="order(processing)" value="5" />
                        <fr:property name="order(send)" value="6" />
                        <fr:property name="order(receiveFrom)" value="7" />
                        <fr:property name="order(print)" value="8" />
                        <fr:property name="order(deliver)" value="9" />
                        <fr:property name="order(conclude)" value="10" />

                        <fr:property name="sortBy" value="requestDate=desc, activeSituation.situationDate=desc, urgentRequest=desc, description=asc"/>
                    </fr:layout>
                </fr:view>
