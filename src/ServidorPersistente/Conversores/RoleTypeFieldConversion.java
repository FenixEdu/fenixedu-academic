/*
 * Created on 11/Mar/2003 by jpvl 
 *
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.RoleType;

/**
 * @author jpvl
 */
public class RoleTypeFieldConversion implements FieldConversion {

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
	 */
	public Object javaToSql(Object obj) throws ConversionException {
		if (obj instanceof RoleType) {
			RoleType roleType = (RoleType) obj;
			return new Integer(roleType.getValue());
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
	 */
	public Object sqlToJava(Object obj) throws ConversionException {
		RoleType roleType = null;
		if (obj instanceof Integer) {
			Integer roleTypeId = (Integer) obj;
			
			roleType = RoleType.getEnum(roleTypeId.intValue());
			if (roleType == null) {
				throw new IllegalArgumentException(this.getClass().getName() + ": Illegal role type!(" + obj + ")");
			}
			
//			
//			switch (roleTypeId.intValue()) {
//				case RoleType.PERSON_TYPE :
//					roleType = RoleType.PERSON;
//					break;
//				case RoleType.STUDENT_TYPE :
//					roleType = RoleType.STUDENT;
//					break;
//				case RoleType.TEACHER_TYPE :
//					roleType = RoleType.TEACHER;
//					break;
//				case RoleType.TIME_TABLE_MANAGER_TYPE :
//					roleType = RoleType.TIME_TABLE_MANAGER;
//					break;
//				case RoleType.MASTER_DEGREE_CANDIDATE_TYPE :
//					roleType = RoleType.MASTER_DEGREE_CANDIDATE;
//					break;
//				case RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE :
//					roleType = RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE;
//					break;
//				case RoleType.TREASURY_TYPE :
//					roleType = RoleType.TREASURY;
//					break;
//				case RoleType.COORDINATOR_TYPE :
//					roleType = RoleType.COORDINATOR;
//					break;
//				case RoleType.EMPLOYEE_TYPE :
//					roleType = RoleType.EMPLOYEE;
//					break;
//				case RoleType.MANAGEMENT_ASSIDUOUSNESS_TYPE:
//					roleType = RoleType.MANAGEMENT_ASSIDUOUSNESS;
//					break;	
//				case RoleType.MANAGER_TYPE:
//					roleType = RoleType.MANAGER;
//					break;
//				case RoleType.DEGREE_ADMINISTRATIVE_OFFICE_TYPE :
//					roleType = RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
//					break;
//				case RoleType.CREDITS_MANAGER_TYPE:
//					roleType = RoleType.CREDITS_MANAGER;
//					break;
//				case RoleType.CREDITS_MANAGER_DEPARTMENT_TYPE:
//					roleType = RoleType.CREDITS_MANAGER_DEPARTMENT;
//					break;
//				default :
//					throw new IllegalArgumentException(this.getClass().getName() + ": Illegal role type!(" + obj + ")");
//
//			}
		} else {
			throw new IllegalArgumentException("Illegal role type!(" + obj + ")");
		}
		return roleType;

	}

}
