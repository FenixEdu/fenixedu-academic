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
package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class ErasmusBolonhaStudentEnrollmentInputRenderer extends BolonhaStudentEnrollmentInputRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ErasmusBolonhaStudentEnrollmentInputRenderer.class);

    @Override
    protected Layout getLayout(Object object, Class type) {
        ErasmusBolonhaStudentEnrolmentLayout thisLayout =
                (ErasmusBolonhaStudentEnrolmentLayout) ((getDefaultLayout() == null) ? new ErasmusBolonhaStudentEnrolmentLayout() : createLayout());
        thisLayout.setRenderer(this);
        return thisLayout;
    }

    private Layout createLayout() {
        try {
            Class<?> clazz = Class.forName(getDefaultLayout());
            return (Layout) clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
