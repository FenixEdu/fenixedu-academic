/**
 * 
 */


package net.sourceforge.fenixedu.domain.cms;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 0:35:00,22/Set/2005
 * @version $Id$
 */
public enum UserGroupTypes
{
	UNION_GROUP
	{
		public Class getImplementationClass()
		{
			return GroupUnion.class;
		}
	},
	INTERSECTION_GROUP
	{
		public Class getImplementationClass()
		{
			return GroupIntersection.class;
		}
	},
	DIFFERENCE_GROUP
	{
		public Class getImplementationClass()
		{
			return GroupDifference.class;
		}
	},
	EXECUTION_COURSE_STUDENTS_GROUP
	{
		public Class getImplementationClass()
		{
			return ExecutionCourseStudentsGroup.class;
		}
	},
	EXECUTION_COURSE_TEACHERS_GROUP
	{
		public Class getImplementationClass()
		{
			return ExecutionCourseTeachersGroup.class;
		}
	},
	PERSON_GROUP
	{
		public Class getImplementationClass()
		{
			return PersonGroup.class;
		}
	};
	abstract public Class getImplementationClass();

	public static UserGroupTypes userGroupTypeByClass(Class clazz) throws InvalidUserGroupTypeException
	{
		UserGroupTypes result = null;
		UserGroupTypes[] types = UserGroupTypes.values();
		for (int i = 0; i < types.length; i++)
		{
			if (types[i].getImplementationClass().equals(clazz)) result = types[i];
		}

		return result;
	}
}
