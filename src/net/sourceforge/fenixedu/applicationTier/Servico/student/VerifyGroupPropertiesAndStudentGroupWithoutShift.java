/*
 * Created on 20/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author joaosa & rmalo
 *  
 */
public class VerifyGroupPropertiesAndStudentGroupWithoutShift implements IServico {

    private static VerifyGroupPropertiesAndStudentGroupWithoutShift _servico = new VerifyGroupPropertiesAndStudentGroupWithoutShift();

    /**
     * The singleton access method of this class.
     */
    public static VerifyGroupPropertiesAndStudentGroupWithoutShift getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private VerifyGroupPropertiesAndStudentGroupWithoutShift() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "VerifyGroupPropertiesAndStudentGroupWithoutShift";
    }
    
    public Integer run(Integer studentGroupCode, Integer groupPropertiesCode,String shiftCodeString,String username)throws FenixServiceException {
    	 IPersistentStudentGroup persistentStudentGroup = null;
         IPersistentGroupProperties persistentGroupProperties = null;
 		  try {

             ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
         
             persistentStudentGroup = ps.getIPersistentStudentGroup();
             
             persistentGroupProperties = ps.getIPersistentGroupProperties();
         
             IGroupProperties groupProperties = (IGroupProperties)persistentGroupProperties.readByOID(
             		GroupProperties.class, groupPropertiesCode);
             
             if(groupProperties == null){
             	throw new ExistingServiceException();
             }
             
             IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
             		StudentGroup.class, studentGroupCode);
             
             if(studentGroup == null){
             	throw new InvalidSituationServiceException();
             }
             
             
             Integer shiftCode = null;
             if(shiftCodeString!=null){
             	shiftCode = new Integer(shiftCodeString);
             }
             
             if(studentGroup.getShift() != null && shiftCode == null){
            	throw new InvalidArgumentsServiceException();
            }
             
             if(studentGroup.getShift() == null){
             	if(shiftCode != null) throw new InvalidArgumentsServiceException();
             }
             else{
             	if(studentGroup.getShift().getIdInternal().intValue() != shiftCode.intValue()){
             		throw new InvalidArgumentsServiceException();
             	}
             }
         
             if(studentGroup.getShift() != null && groupProperties.getShiftType() != null){
             	return new Integer(1);
             }
             
             if(studentGroup.getShift() != null && groupProperties.getShiftType() == null){
             	return new Integer (2);
             }
             
             if(studentGroup.getShift()==null && groupProperties.getShiftType()!=null){
             	return new Integer(3);
             }
             
             if(studentGroup.getShift() == null && groupProperties.getShiftType() == null){
             	return new Integer (4);
             }
             
             
                         
         } catch (ExcepcaoPersistencia excepcaoPersistencia) {
             throw new FenixServiceException(excepcaoPersistencia.getMessage());
         }

         return new Integer(5);

     }
 }
