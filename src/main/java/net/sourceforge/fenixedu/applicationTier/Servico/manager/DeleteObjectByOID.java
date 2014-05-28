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
/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DeleteObjectByOID {

    @Atomic
    public static Boolean run(String externalId) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        try {
            MethodUtils.invokeMethod(FenixFramework.getDomainObject(externalId), "delete", null);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                if (e.getTargetException() instanceof WriteOnReadError) {
                    throw ((WriteOnReadError) e.getTargetException());
                }
                throw new FenixServiceException(e.getTargetException());
            }
            throw new FenixServiceException(e);
        } catch (NoSuchMethodException e) {
            throw new FenixServiceException(e);
        } catch (IllegalAccessException e) {
            throw new FenixServiceException(e);
        }

        return true;
    }
}