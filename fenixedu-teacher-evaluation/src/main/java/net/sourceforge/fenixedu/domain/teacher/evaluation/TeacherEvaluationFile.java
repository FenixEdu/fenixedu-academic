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
package org.fenixedu.academic.domain.teacher.evaluation;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.fenixedu.academic.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

public class TeacherEvaluationFile extends TeacherEvaluationFile_Base {

    public TeacherEvaluationFile(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType,
            String filename, byte[] content, Person createdBy) {
        super();
        setTeacherEvaluation(teacherEvaluation);
        setTeacherEvaluationFileType(teacherEvaluationFileType);
        setCreatedBy(createdBy);
        filename = processFilename(teacherEvaluation, teacherEvaluationFileType, getExtension(filename));

        final Set<Group> groups = new HashSet<>();

        final TeacherEvaluationProcess process = teacherEvaluation.getTeacherEvaluationProcess();
        groups.add(UserGroup.of(process.getEvaluator().getUser()));
        groups.add(UserGroup.of(process.getEvaluee().getUser()));
        PersistentGroup group = Bennu.getInstance().getTeacherEvaluationCoordinatorCouncil();
        if (group != null) {
            groups.add(group.toGroup());
        }

        init(filename, filename, content, UnionGroup.of(groups));
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "txt" : filename.substring(dot + 1, filename.length());
    }

    private String processFilename(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType,
            String extension) {
        List<String> parts = new Vector<String>(4);
        if (!teacherEvaluationFileType.isAutoEvaluationFile()) {
            parts.add("res");
        }
        parts.add(teacherEvaluation.getFilenameTypePrefix());
        try {
            parts.add("d"
                    + teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher().getPerson().getEmployee()
                            .getEmployeeNumber());
        } catch (NullPointerException e) {
            parts.add(teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getUsername());
        }
        parts.add(teacherEvaluation.getTeacherEvaluationProcess().getFacultyEvaluationProcess().getSuffix());
        return StringUtils.join(parts, "_") + "." + extension;
    }

    @Override
    public void delete() {
        setTeacherEvaluation(null);
        setCreatedBy(null);
        super.delete();
    }

    @Atomic
    public static TeacherEvaluationFile create(FileUploadBean fileUploadBean, Person createdBy) throws IOException {
        return new TeacherEvaluationFile(fileUploadBean.getTeacherEvaluationProcess().getCurrentTeacherEvaluation(),
                fileUploadBean.getTeacherEvaluationFileType(), fileUploadBean.getFilename(), fileUploadBean.getBytes(), createdBy);
    }

    public TeacherEvaluationFile copy(TeacherEvaluation evaluation) {
        return new TeacherEvaluationFile(evaluation, getTeacherEvaluationFileType(), getFilename(), getContents(), getCreatedBy());
    }

}
