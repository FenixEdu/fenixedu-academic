/*
 * Created on 23/Jul/2003
 *
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */

public class InfoSiteExercise extends DataTranferObject implements ISiteComponent
{
	private InfoMetadata infoMetadata;
	private InfoExecutionCourse executionCourse;

	public InfoSiteExercise()
	{
	}

	public InfoExecutionCourse getExecutionCourse()
	{
		return executionCourse;
	}

	public InfoMetadata getInfoMetadata()
	{
		return infoMetadata;
	}

	public void setExecutionCourse(InfoExecutionCourse course)
	{
		executionCourse = course;
	}

	public void setInfoMetadata(InfoMetadata metadata)
	{
		infoMetadata = metadata;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof InfoSiteExercise)
		{
			InfoSiteExercise infoSiteMetadata = (InfoSiteExercise) obj;
			result =
				getExecutionCourse().equals(infoSiteMetadata.getExecutionCourse())
					&& getInfoMetadata().equals(infoSiteMetadata.getInfoMetadata());
		}
		return result;
	}
}
