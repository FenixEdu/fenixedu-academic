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
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Factory
 * 
 */
public class ScientificCouncilCurricularCourseCurriculumComponentBuilder {

    private static ScientificCouncilCurricularCourseCurriculumComponentBuilder instance = null;

    public ScientificCouncilCurricularCourseCurriculumComponentBuilder() {
    }

    public static ScientificCouncilCurricularCourseCurriculumComponentBuilder getInstance() {
        if (instance == null) {
            instance = new ScientificCouncilCurricularCourseCurriculumComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, String curricularCourseId, Integer curriculumId)
            throws FenixServiceException {
        if (component instanceof InfoSiteCurriculum) {
            return getInfoSiteCurriculum((InfoSiteCurriculum) component, curricularCourseId);
        }
        return null;

    }

    /**
     * @param curriculum
     * @param curricularCourseId
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteCurriculum(InfoSiteCurriculum component, String curricularCourseId)
            throws FenixServiceException {
        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);

        Curriculum curriculum = curricularCourse.findLatestCurriculum();

        if (curriculum != null) {
            InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
            component.setInfoCurriculum(infoCurriculum);
        }
        component.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

        return component;
    }

}