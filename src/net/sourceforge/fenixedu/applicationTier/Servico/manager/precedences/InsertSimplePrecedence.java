/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByNumberOfCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionPeriodToApply;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertSimplePrecedence implements IService {

    public InsertSimplePrecedence() {
    }

    public void run(String className, Integer curricularCourseToAddPrecedenceID,
            Integer precedentCurricularCourseID, Integer number) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();

            ICurricularCourse curricularCourseToAddPrecedence = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseToAddPrecedenceID);
            if (curricularCourseToAddPrecedence == null) {
                throw new FenixServiceException("curricularCourseToAddPrecedence.NULL");
            }

            IPersistentPrecedence persistentPrecedence = persistentSuport.getIPersistentPrecedence();
            IPrecedence precedence = new Precedence();
            persistentPrecedence.lockWrite(precedence);
            precedence.setCurricularCourse(curricularCourseToAddPrecedence);

            ICurricularCourse precedentCurricularCourse = null;
            if (precedentCurricularCourseID != null) {
                precedentCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                        CurricularCourse.class, precedentCurricularCourseID);
                if (precedentCurricularCourse == null) {
                    throw new FenixServiceException("precedentCurricularCourse.NULL");
                }
            }

            IPersistentRestriction persistentRestriction = persistentSuport.getIPersistentRestriction();
            if (className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName().substring(
                    RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionByNumberOfCurricularCourses restriction = new RestrictionByNumberOfDoneCurricularCourses();
                restriction.setNumberOfCurricularCourses(number);
                restriction.setPrecedence(precedence);

                persistentRestriction.lockWrite(restriction);

            } else if (className.equals(RestrictionPeriodToApply.class.getName().substring(
                    RestrictionPeriodToApply.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionPeriodToApply restrictionPeriodToApply = new RestrictionPeriodToApply();
                restrictionPeriodToApply.setPrecedence(precedence);
                restrictionPeriodToApply.setPeriodToApplyRestriction(PeriodToApplyRestriction
                        .getEnum(number.intValue()));

                persistentRestriction.lockWrite(restrictionPeriodToApply);

            } else if (className.equals(RestrictionDoneCurricularCourse.class.getName().substring(
                    RestrictionDoneCurricularCourse.class.getName().lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionDoneCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

                persistentRestriction.lockWrite(restrictionByCurricularCourse);

            } else if (className.equals(RestrictionNotDoneCurricularCourse.class.getName().substring(
                    RestrictionNotDoneCurricularCourse.class.getName().lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionNotDoneCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

                persistentRestriction.lockWrite(restrictionByCurricularCourse);

            } else if (className
                    .equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                            .getName().substring(
                                    RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                                            .getName().lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

                persistentRestriction.lockWrite(restrictionByCurricularCourse);

            } else if (className
                    .equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                            .getName()
                            .substring(
                                    RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                                            .getName().lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

                persistentRestriction.lockWrite(restrictionByCurricularCourse);
            } else if (className.equals(RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class
                    .getName().substring(
                            RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName()
                                    .lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

                persistentRestriction.lockWrite(restrictionByCurricularCourse);

            } else if (className
                    .equals(RestrictionNotEnrolledInCurricularCourse.class.getName()
                            .substring(
                                    RestrictionNotEnrolledInCurricularCourse.class.getName()
                                            .lastIndexOf(".") + 1))
                    && precedentCurricularCourse != null) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionNotEnrolledInCurricularCourse();
                persistentRestriction.lockWrite(restrictionByCurricularCourse);
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);

            } else {
                throw new FenixServiceException("error.manager.impossible.insertPrecedence");
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
    }
}