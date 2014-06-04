/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.executionCourse;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.CreateCourseReports;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCourseReportsForExecutionPeriod extends FenixBackingBean {

    private String executionPeriodID;

    public List getExecutionPeriods() throws FenixServiceException {

        List executionPeriods = ReadNotClosedExecutionPeriods.run();

        CollectionUtils.transform(executionPeriods, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) arg0;
                return new SelectItem(executionPeriod.getExternalId(), executionPeriod.getDescription());
            }

        });

        return executionPeriods;

    }

    public void create(ActionEvent evt) throws FenixServiceException {
        CreateCourseReports.run(getExecutionPeriodID());
        addInfoMessage(BundleUtil.getString(Bundle.MANAGER, "message.manager.createCourseReports.success"));
    }

    public String getExecutionPeriodID() {
        return executionPeriodID;
    }

    public void setExecutionPeriodID(String executionPeriodID) {
        this.executionPeriodID = executionPeriodID;
    }

}