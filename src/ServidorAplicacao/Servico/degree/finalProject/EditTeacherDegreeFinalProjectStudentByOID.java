/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import DataBeans.InfoObject;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IStudent;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class EditTeacherDegreeFinalProjectStudentByOID extends EditDomainObjectService
{
    /**
	 * @author jpvl
	 */
    public class StudentNotFoundServiceException extends FenixServiceException
    {

        /**
		 *  
		 */
        public StudentNotFoundServiceException()
        {
            super();
        }
    }
    private static EditTeacherDegreeFinalProjectStudentByOID service = new EditTeacherDegreeFinalProjectStudentByOID();

    /**
	 * The singleton access method of this class.
	 */
    public static EditTeacherDegreeFinalProjectStudentByOID getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
	 */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;
        return Cloner.copyInfoTeacherDegreeFinalProjectStudent2ITeacherDegreeFinalProjectStudent(
                infoTeacherDegreeFinalProjectStudent);
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentTeacherDegreeFinalProjectStudent();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditTeacherDegreeFinalProjectStudentByOID";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(Dominio.IDomainObject,
	 *          ServidorPersistente.ISuportePersistente)
	 */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia, FenixServiceException
    {
        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = (ITeacherDegreeFinalProjectStudent) domainObject;
        IStudent student = teacherDegreeFinalProjectStudent.getStudent();
        IPersistentStudent studentDAO = sp.getIPersistentStudent();

        student = studentDAO.readByNumero(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);
        if (student == null)
        {
            throw new StudentNotFoundServiceException();
        }
        teacherDegreeFinalProjectStudent.setStudent(student);

        IPersistentTeacherDegreeFinalProjectStudent teacherDFPStudentDAO = sp
                .getIPersistentTeacherDegreeFinalProjectStudent();

        teacherDegreeFinalProjectStudent = teacherDFPStudentDAO.readByUnique(
                teacherDegreeFinalProjectStudent);

        return teacherDegreeFinalProjectStudent;
    }

}