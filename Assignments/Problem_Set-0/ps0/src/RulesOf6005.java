import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;


/**
 * RulesOf6005 represents some of the rules of 6.005 as described by the 
 * general information on Stellar. 
 * 
 * The course elements are described by the hasFeature function. 
 * 
 * The grade details are described by the computeGrade function.
 * 
 * The extension policy (slack days) are described by the extendDeadline function.
 */
public class RulesOf6005 {
	
	
	/**
	 * Tests if the string is one of the items in the Course Elements section. 
	 *  
	 * @param name - the element to be tested
	 * @return true if <name> appears in bold in Course Elements section. Ignores case (capitalization). 
	 * Example: "Lectures" and "lectures" will both return true.
	 */
	public static boolean hasFeature(String name){
		String target = "<strong>" + name + "</strong>";
		int targetLength = target.length();
		final String courseUrlString =
			"http://stellar.mit.edu/S/course/6/fa11/6.005/courseMaterial/topics/topic1/syllabus/text/text";
		try {
			// fetch the syllabus page of course 6.005
			String courseSyllabus = WebUtils.fetch(courseUrlString);
			
			// create a stream that read data into a buffered string
			StringReader syllabusStrReader = new StringReader(courseSyllabus);
			BufferedReader syllabusBuffReader = new BufferedReader(syllabusStrReader);
			
			// search the syllabus page line by line for the feature name
			String lineContents = syllabusBuffReader.readLine();
			while (lineContents != null) {
				int lineLength = lineContents.length();
				int searchingBound = lineLength - targetLength;
				for (int i = 0; i <= searchingBound; i = i + 1) {
					if (lineContents.regionMatches(true, i, target, 0, targetLength)) {
						return true;
					}
				}
				lineContents = syllabusBuffReader.readLine();
			}
			// don't forget closing the stream at the end to free up resources
			syllabusBuffReader.close();
			syllabusStrReader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Takes in the quiz, pset, project, and participation grades as values out of a 
	 * hundred and returns the grade based on the course information also as a value out 
	 * of a hundred, rounded to the nearest integer. 
	 * 
	 * Behavior is unspecified if the values are out of range.
	 * 
	 * @param quiz
	 * @param pset
	 * @param project
	 * @param participation
	 * @return the resulting grade out of a hundred
	 */
	public static int computeGrade(int quiz, int pset, int project, int participation){
		int grade = 0;
		
		final double QUIZ_WEIGHT = 0.20;
		final double PSET_WEIGHT = 0.40;
		final double PROJ_WEIGHT = 0.30;
		final double PART_WEIGHT = 0.10;	
		grade = (int) (Math.round
				(((quiz * QUIZ_WEIGHT)
				+ (pset * PSET_WEIGHT)
				+ (project * PROJ_WEIGHT)
				+ (participation * PART_WEIGHT))));
		return grade;
	}
	
	
	/**
	 * Based on the slack day policy, returns a date of when the assignment would be due, making sure not to
	 * exceed the budget. In the case of the request being more than what's allowed, the latest possible
	 * due date is returned. 
	 * 
	 * Hint: Take a look at http://download.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html
	 * 
	 * Behavior is unspecified if request is negative or duedate is null. 
	 * 
	 * @param request - the requested number of slack days to use
	 * @param budget - the total number of available slack days
	 * @param duedate - the original due date of the assignment
	 * @return a new instance of a Calendar with the date and time set to when the assignment will be due
	 */
	public static Calendar extendDeadline(int request, int budget, Calendar duedate){
		Calendar extendedDuedate = new GregorianCalendar();
		extendedDuedate.clear();
		extendedDuedate.setTime(duedate.getTime());
		final int MAX_SLACK_DAY = 3;
		int extension = MathUtils.tripleMin(request, budget, MAX_SLACK_DAY);
		extendedDuedate.add(Calendar.DATE, extension);
		return extendedDuedate;
	}
	
	
	/**
	 * Main method of the class. Runs the functions hasFeature, computeGrade, and 
	 * extendDeadline. 
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("Has feature QUIZZES: " + hasFeature("QUIZZES"));
		System.out.println("My grade is: " + computeGrade(60, 40, 50, 37));
		Calendar duedate = new GregorianCalendar();
		duedate.set(2011, 8, 9, 23, 59, 59);
		System.out.println("Original due date: " + duedate.getTime());
		System.out.println("New due date:" + extendDeadline(4, 2, duedate).getTime());
	}
}
