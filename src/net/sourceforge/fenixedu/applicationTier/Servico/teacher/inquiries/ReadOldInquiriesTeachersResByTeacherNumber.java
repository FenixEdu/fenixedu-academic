/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiriesTeachersResByTeacherNumber extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static List run(String teacherId) throws FenixServiceException {
        Teacher teacher = Teacher.readByIstId(teacherId);

        if (teacher == null) {
            throw new FenixServiceException("nullTeacherNumber");
        }

        List<OldInquiriesTeachersRes> oldInquiriesTeachersResList = teacher.getAssociatedOldInquiriesTeachersRes();

        CollectionUtils.transform(oldInquiriesTeachersResList, new Transformer() {

            @Override
            public Object transform(Object oldInquiriesTeachersRes) {
                InfoOldInquiriesTeachersRes ioits = new InfoOldInquiriesTeachersRes();
                try {
                    ioits.copyFromDomain((OldInquiriesTeachersRes) oldInquiriesTeachersRes);

                } catch (Exception ex) {
                }

                return ioits;
            }
        });

        return oldInquiriesTeachersResList;
    }

}