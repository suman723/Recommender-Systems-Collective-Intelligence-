/**
 * Compute the Increasing Confidence Similarity Metric between (item) profiles.
 */

package similarity.metric.profile;


import java.util.Set;
import similarity.metric.SimilarityMetric;
import profile.Profile;


public class IncConfidenceMetric implements SimilarityMetric<Profile>
{	

	private static double RATING_THRESHOLD = 4.0; // the threshold rating for liked items 



	/**
	 * constructor - creates a new CosineMetric object
	 */
	public IncConfidenceMetric()
	{
	}


	/**
	 * Computes the similarity between two profiles using the Increasing Confidence Similarity Metric.
	 * @param p1 Profile 1
	 * @param p2 Profile 2
	 * @return similarity value between p1 and p2
	 */
	@Override
	public double getSimilarity(final Profile p1, final Profile p2)
	{
		// Calculate similarity using conf(X => Y) / conf(!X => Y)		
		int suppXY = 0; // number of users who rated both X and Y >= RATING_THRESHOLD
		int suppX = 0; // number of users who rated X >= RATING_THRESHOLD
		int suppXnotY = 0; // number of users who rated X < RATING_THRESHOLD and Y >= RATING_THRESHOLD
		int nX = 0; // number of users who rated X or Y
		// Get the set of common IDs between p1 and p2
		Set<Integer> ids = p1.getCommonIds(p2);

		// count the number of users who rated X or Y
		for (Integer id : ids) {
			Double r1 = p1.getValue(id);
			Double r2 = p2.getValue(id);
			//counts the number of users who have rated at least one item in the two profiles.
			if (r1 != null || r2 != null) {
				nX++;
			}
			//suppXY: counts the number of users who have rated both items in the two profiles and have given a 
			//rating greater than or equal to the threshold RATING_THRESHOLD
			if (r1 != null && r2 != null && r1 >= RATING_THRESHOLD && r2 >= RATING_THRESHOLD) {
				suppXY++;
			}
			//suppX: counts the number of users who have rated the first item in the profile and have given a 
			//rating greater than or equal to the threshold RATING_THRESHOLD.
			if (r1 != null && r1 >= RATING_THRESHOLD) {
				suppX++;
				if (r2 != null && r2 < RATING_THRESHOLD) {
					//counts the number of users who have rated the first item in the profile and have given a rating greater 
					//than or equal to the threshold RATING_THRESHOLD,
					//but have rated the second item in the profile with a rating less than RATING_THRESHOLD.
					suppXnotY++;
				}
			}
		}

		// calculate conf(X => Y)
		double confXtoY = 0.0;
		if (suppX > 0 && suppXY > 0) {
			confXtoY = (double)suppXY / suppX;
		}

		// calculate conf(!X => Y)
		double confNotXtoY = 0.0;
		if (nX - suppX > 0 && suppXnotY > 0) {
			confNotXtoY = (double)suppXnotY / (nX - suppX);
		}

		// Handling denominator = 0
		// calculate similarity 
		if (confNotXtoY > 0) {
			return confXtoY / confNotXtoY;
		} else {
			return 0.0;
		}
	}



}
