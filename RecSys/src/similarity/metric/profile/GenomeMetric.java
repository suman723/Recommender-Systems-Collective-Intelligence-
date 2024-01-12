/**
 * Compute the Increasing Confidence Similarity Metric between (item) profiles.
 */

package similarity.metric.profile;

import java.util.Set;

import profile.Profile;
import similarity.metric.SimilarityMetric;


public class GenomeMetric implements SimilarityMetric<Profile>
{		
	/**
	 * constructor - creates a new CosineMetric object
	 */
	public GenomeMetric()
	{
	}

	/**
	 * Computes the similarity between two profiles using weighted Jaccard similarity
	 * @param genomeX - the first profile
	 * @param genomeY - the second profile
	 * @return the similarity between the two profiles
	 */

	@Override
	public double getSimilarity(final Profile genomeX, final Profile genomeY) {
		// calculate similarity using weighted Jaccard
		double numerator = 0.0;
		double denominator = 0.0;
		// Iterate through all the common items in the two profiles

		for (Integer id : genomeX.getCommonIds(genomeY)) {
			double weight = 1.0; // assume all weights are equal
			// get the minimum and maximum score for the item in the two profiles
			double minScore = Math.min(genomeX.getValue(id), genomeY.getValue(id));
			double maxScore = Math.max(genomeX.getValue(id), genomeY.getValue(id));
			// add the weighted minimum score to the numerator and weighted maximum score to the denominator
			numerator += weight * minScore;
			denominator += weight * maxScore;
		}

		return numerator / denominator;
	}



}
