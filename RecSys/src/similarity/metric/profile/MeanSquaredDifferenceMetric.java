/**
 * Compute the Mean Squared Difference similarity between profiles.
 */

package similarity.metric.profile;

import java.util.Set;
import similarity.metric.SimilarityMetric;

import profile.Profile;

public class MeanSquaredDifferenceMetric implements SimilarityMetric<Profile>
{
	private double rmax;
	private double rmin;
	/**
	 * constructor - creates a new MeanSquaredDifferenceMetric object
	 */
	public MeanSquaredDifferenceMetric(double a,double b)
	{
		this.rmin = a;
		this.rmax = b;

		/* enforce a difference of 1 if the two bounds are equal */
		if (a==b)
			this.rmax = b+1;
	}

	/**
	 * computes the similarity between profiles
	 * @param profile 1 
	 * @param profile 2
	 */
	@Override
	public double getSimilarity(final Profile profile1, final Profile profile2)
	{	// compute the sum of the squared differences between the ratings for each common item
		double squaredDiffSum = 0;
	
		// loop through each common item between the two profiles
		Set<Integer> commonItemIds = profile1.getCommonIds(profile2);
		for (Integer itemId : commonItemIds) {
			Double profile1Value = profile1.getValue(itemId);
			Double profile2Value = profile2.getValue(itemId);

			// get the ratings for the current item from each profile
			// if either profile is missing a rating for the item, skip it
			if (profile1Value != null && profile2Value != null) {
				// compute the difference between the two ratings
				double diff = profile1Value - profile2Value;
				squaredDiffSum += diff * diff;
			}
		}

		// find sqrd difference
		double meanSquaredDiff = commonItemIds.size() > 0 ? squaredDiffSum / commonItemIds.size() : 0;
		return 1 / (1 + meanSquaredDiff);
	}



}
