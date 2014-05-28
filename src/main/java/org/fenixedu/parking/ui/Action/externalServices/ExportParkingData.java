/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of Fenix Parking.
 *
 * Fenix Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fenix Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.parking.ui.Action.externalServices;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.parking.ParkingConfigurationManager;
import org.fenixedu.parking.domain.reports.ParkingDataReportFile;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.common.collect.Ordering;

@Mapping(module = "external", path = "/exportParkingData")
public class ExportParkingData extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String password = request.getParameter("password");
        final String username = request.getParameter("username");
        checkPermissions(username, password);

        ParkingDataReportFile parkingDataReportFile = getLastParkingDataReportJob();
        if (parkingDataReportFile != null && parkingDataReportFile.getFile() != null) {
            final byte[] data = parkingDataReportFile.getFile().getContent();
            response.setContentLength(data.length);
            response.setContentType("application/vnd.ms-access");
            response.setHeader("Content-disposition", "attachment; filename=" + parkingDataReportFile.getFilename());
            final ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
        return null;
    }

    private ParkingDataReportFile getLastParkingDataReportJob() {
        Set<QueueJob> parkingJobs = new HashSet<QueueJob>();
        for (QueueJob queueJob : Bennu.getInstance().getQueueJobSet()) {
            if (queueJob instanceof ParkingDataReportFile && queueJob.getDone()) {
                parkingJobs.add(queueJob);
            }
        }

        return parkingJobs.size() > 0 ? (ParkingDataReportFile) Ordering.from(new BeanComparator("requestDate")).max(parkingJobs) : null;
    }

    private void checkPermissions(String username, String password) throws NotAuthorizedException {
        final String allowedUser = ParkingConfigurationManager.getConfiguration().getExportParkingDataUsername();
        final String allowedPass = ParkingConfigurationManager.getConfiguration().getExportParkingDataPassword();
        if ((!allowedUser.equals(username)) || (!allowedPass.equals(password))) {
            throw new NotAuthorizedException();
        }
    }

    private static final class NotAuthorizedException extends RuntimeException {
        private static final long serialVersionUID = -2836175455347962317L;
    }

}