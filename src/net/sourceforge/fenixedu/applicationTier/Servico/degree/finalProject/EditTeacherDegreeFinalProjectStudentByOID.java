/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentByOID extends EditDomainObjectService {
    /**
     * @author jpvl
     */
    public class ExecutionPeriodNotFoundException extends FenixServiceException {

        /**
         *  
         */
        public ExecutionPeriodNotFoundException() {
            super();
        }
    }

    /**
     * @author jpvl
     */
    public class StudentNotFoundServiceException extends FenixServiceException {
        public StudentNotFoundServiceException() {
            super();
        }
    }

    /**
     * @author jpvl
     */
    public class StudentPercentageExceed extends FenixServiceException {
        private List infoTeacherDegreeFinalProjectStudentList;

        /**
         * @param infoTeacherDegreeFinalProjectStudentList
         */
        public StudentPercentageExceed(List infoTeacherDegreeFinalProjectStudentList) {
            this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
        }

        /**
         * @return Returns the infoTeacherDegreeFinalProjectStudentList.
         */
        public List getInfoTeacherDegreeFinalProjectStudentList() {
            return this.infoTeacherDegreeFinalProjectStudentList;
        }
    }

    private static EditTeacherDegreeFinalProjectStudentByOID service = new EditTeacherDegreeFinalProjectStudentByOID();

    /**
     * The singleton access method of this class.
     */
    public static EditTeacherDegreeFinalProjectStudentByOID getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;
        return Cloner
                .copyInfoTeacherDegreeFinalProjectStudent2ITeacherDegreeFinalProjectStudent(infoTeacherDegreeFinalProjectStudent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#doBeforeLock(Dominio.IDomainObject,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject, ServidorPersistente.ISuportePersistente)
     */
    protected void doBeforeLock(IDomainObject domainObjectToLock, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException {
        try {
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;

            InfoStudent infoStudent = infoTeacherDegreeFinalProjectStudent.getInfoStudent();

            IPersistentStudent studentDAO = sp.getIPersistentStudent();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            IStudent student = studentDAO.readStudentByNumberAndDegreeType(infoStudent.getNumber(),
                    DegreeType.DEGREE);

            IExecutionPeriod executionPeriod = new ExecutionPeriod(infoTeacherDegreeFinalProjectStudent
                    .getInfoExecutionPeriod().getIdInternal());
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    infoTeacherDegreeFinalProjectStudent.getInfoExecutionPeriod().getIdInternal());

            if (student == null) {
                throw new StudentNotFoundServiceException();
            }
            if (executionPeriod == null) {
                throw new ExecutionPeriodNotFoundException();
            }

            IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO = sp
                    .getIPersistentTeacherDegreeFinalProjectStudent();
            List teacherDegreeFinalProjectStudentList = teacherDegreeFinalProjectStudentDAO
                    .readByStudentAndExecutionPeriod(student, executionPeriod);

            double requestedPercentage = ((InfoTeacherDegreeFinalProjectStudent) infoObject)
                    .getPercentage().doubleValue();

            Iterator iterator = teacherDegreeFinalProjectStudentList.iterator();
            double percentage = requestedPercentage;
            while (iterator.hasNext()) {
                ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) iterator
                        .next();

                if (teacherDegreeFinalProjectStudent.getTeacher().getIdInternal().intValue() != infoTeacherDegreeFinalProjectStudent
                        .getInfoTeacher().getIdInternal().intValue()) {
                    percentage += teacherDegreeFinalProjectStudent.getPercentage().doubleValue();
                }
            }
            if (percentage > 100) {
                List infoTeacherDegreeFinalProjectStudentList = (List) CollectionUtils.collect(
                        teacherDegreeFinalProjectStudentList, new Transformer() {

                            public Object transform(Object input) {
                                ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) input;
                                InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = Cloner
                                        .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(teacherDegreeFinalProjectStudent);
                                return infoTeacherDegreeFinalProjectStudent;
                            }
                        });
                throw new StudentPercentageExceed(infoTeacherDegreeFinalProjectStudentList);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
    } /*
       * (non-Javadoc)
       * 
       * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
       */

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditTeacherDegreeFinalProjectStudentByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia, FenixServiceException {
        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) domainObject;
        IStudent student = teacherDegreeFinalProjectStudent.getStudent();
        IPersistentStudent studentDAO = sp.getIPersistentStudent();

        student = studentDAO.readStudentByNumberAndDegreeType(student.getNumber(),
                DegreeType.DEGREE);
        if (student == null) {
            throw new StudentNotFoundServiceException();
        }
        teacherDegreeFinalProjectStudent.setStudent(student);

        IPersistentTeacherDegreeFinalProjectStudent teacherDFPStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        teacherDegreeFinalProjectStudent = teacherDFPStudentDAO
                .readByUnique(teacherDegreeFinalProjectStudent);

        return teacherDegreeFinalProjectStudent;
    }
}