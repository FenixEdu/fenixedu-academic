/*
 * Created on 30/Jul/2004
 *
 */
package ServidorAplicacao.Servico.manager.precedences;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.precedences.IPrecedence;
import Dominio.precedences.IRestrictionByCurricularCourse;
import Dominio.precedences.IRestrictionByNumberOfCurricularCourses;
import Dominio.precedences.IRestrictionPeriodToApply;
import Dominio.precedences.Precedence;
import Dominio.precedences.RestrictionByNumberOfDoneCurricularCourses;
import Dominio.precedences.RestrictionDoneCurricularCourse;
import Dominio.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import Dominio.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import Dominio.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import Dominio.precedences.RestrictionNotDoneCurricularCourse;
import Dominio.precedences.RestrictionPeriodToApply;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodToApplyRestriction;

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

            IPrecedence precedence = new Precedence();
            precedence.setCurricularCourse(curricularCourseToAddPrecedence);
            IPersistentPrecedence persistentPrecedence = persistentSuport.getIPersistentPrecedence();
            persistentPrecedence.lockWrite(precedence);

            ICurricularCourse precedentCurricularCourse = null;
            if (precedentCurricularCourseID != null) {
                precedentCurricularCourse = (ICurricularCourse) persistentCurricularCourse
                        .readByOID(CurricularCourse.class, precedentCurricularCourseID);
                if (precedentCurricularCourse == null) {
                    throw new FenixServiceException("precedentCurricularCourse.NULL");
                }
            }

            IPersistentRestriction persistentRestriction = persistentSuport.getIPersistentRestriction();
            if (className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName().substring(
                    RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionByNumberOfCurricularCourses restrictionByNumberOfCurricularCourses = new RestrictionByNumberOfDoneCurricularCourses();
                restrictionByNumberOfCurricularCourses.setNumberOfCurricularCourses(number);
                restrictionByNumberOfCurricularCourses.setPrecedence(precedence);
                
                persistentRestriction.lockWrite(restrictionByNumberOfCurricularCourses);
                
            } else if (className.equals(RestrictionPeriodToApply.class.getName().substring(
                    RestrictionPeriodToApply.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionPeriodToApply restrictionPeriodToApply = new RestrictionPeriodToApply();
                restrictionPeriodToApply.setPrecedence(precedence);
                restrictionPeriodToApply.setPeriodToApplyRestriction(new PeriodToApplyRestriction(number.intValue()));
                             
                
                persistentRestriction.lockWrite(restrictionPeriodToApply);
                
            } else if (className.equals(RestrictionDoneCurricularCourse.class.getName().substring(
                    RestrictionDoneCurricularCourse.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionDoneCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);
                
                persistentRestriction.lockWrite(restrictionByCurricularCourse);
                
            } else if (className.equals(RestrictionNotDoneCurricularCourse.class.getName().substring(
                    RestrictionNotDoneCurricularCourse.class.getName().lastIndexOf(".") + 1))) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionNotDoneCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);
                
                persistentRestriction.lockWrite(restrictionByCurricularCourse);
                
             } else if (className.equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                            .getName().substring(
                                    RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class
                                            .getName().lastIndexOf(".") + 1))) {
                IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
                restrictionByCurricularCourse.setPrecedence(precedence);
                restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);
                
                persistentRestriction.lockWrite(restrictionByCurricularCourse);
                
           } else if (className
                            .equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                                    .getName()
                                    .substring(
                                            RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class
                                                    .getName().lastIndexOf(".") + 1))) {
               IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse();
               restrictionByCurricularCourse.setPrecedence(precedence);
               restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);
               
               persistentRestriction.lockWrite(restrictionByCurricularCourse);
           } else if (className.equals(RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName().substring(
                   RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName().lastIndexOf(".") + 1))) {
               IRestrictionByCurricularCourse restrictionByCurricularCourse = new RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse();
               restrictionByCurricularCourse.setPrecedence(precedence);
               restrictionByCurricularCourse.setPrecedentCurricularCourse(precedentCurricularCourse);
               
               persistentRestriction.lockWrite(restrictionByCurricularCourse);
               
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
    }}