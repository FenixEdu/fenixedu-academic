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
/*
 * Created on Nov 28, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
public class ReadTeacherByOID {

    protected InfoObject run(String objectId) {
        final String externalId = objectId;
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        InfoObject infoObject = null;

        if (domainObject != null) {
            infoObject = InfoTeacher.newInfoFromDomain((Teacher) domainObject);
        }

        return infoObject;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherByOID serviceInstance = new ReadTeacherByOID();

    @Atomic
    public static InfoObject runReadTeacherByOID(String externalId) {
        return serviceInstance.run(externalId);
    }

}