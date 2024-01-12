package alg.np;


import java.util.List;
import java.util.Set;

import alg.Recommender;
import profile.Profile;
import util.reader.DatasetReader;

import org.apache.commons.math3.distribution.NormalDistribution;



public class PopularityRecommender extends Recommender {
	// threshold above which a rating is considered to be an upvote
	private Double ratingThreshold; 
	// significance level for Wilson score
	private Double significanceLevel;

	// a profile of scores used to sort the items for recommendation
	private Profile scores;

	/**
	 * constructor - creates a new PopularityRecommender object
	 * @param reader - dataset reader
	 * @param ratingThreshold 
	 * @param significanceLevel
	 */
	public PopularityRecommender(final DatasetReader reader, double ratingThreshold,
			double significanceLevel) {
		super(reader);
		this.ratingThreshold = ratingThreshold;
		this.significanceLevel = significanceLevel;	
		setScores();
	}
	/**
	 * constructor - creates a new PopularityRecommender object
	 * @param reader - dataset reader
	 * @param ratingThreshold 
	 * 
	 */
	public PopularityRecommender(final DatasetReader reader, double ratingThreshold) {
		super(reader);
		this.ratingThreshold = ratingThreshold;
		this.significanceLevel = 1.0;
		setScores();	
	}

	public void setSignificanceLevel(double level)
	{
		// scores are  recomputed whenever the significance level is
		// changed
		this.significanceLevel = level;
		setScores();
	}
	public void setRatingThreshold(double threshold)
	{
		// scores are  recomputed whenever the rating threshold is
		// changed
		this.ratingThreshold = threshold;
		setScores();
	}

	private void setScores() {

		// Create a new profile for the scores
		scores = new Profile(0);


		// Get all the items in the dataset
		Set<Integer> itemIds = reader.getItems().keySet();


		// For each item, calculate the proportion of up-votes and apply the Wilson score formula to correct it
		for (Integer item : itemIds) {
			int totalVotes = 0;
			int upVotes = 0;
			for (Profile profile : reader.getUserProfiles().values()) {
				if (profile.contains(item)) {
					totalVotes++;
					if (profile.getValue(item) >= ratingThreshold) {
						upVotes++;
					}
				}
			}
			// Calculate the proportion of up-votes
			double phat = (double) upVotes / totalVotes;
			// Calculate the z-score for the given significance level
			double z = - new NormalDistribution().inverseCumulativeProbability(significanceLevel / 2);
			// Apply the Wilson score formula to correct the phat
			double wilsonScore = (phat + (z * z) / (2 * totalVotes) - z * Math.sqrt((phat * (1 - phat) +
					z * z / (4 * totalVotes)) / totalVotes)) /
					(1 + (z * z) / totalVotes);
			// save the score across each item
			scores.addValue(item, wilsonScore);


		}}

	public Profile getRecommendationScores(Integer userId)
	{
		return scores;
	}

	/**
	 * @returns the recommendations based on the target item
	 * @param itemId - the target item ID
	 */
	public List<Integer> getRecommendations(Integer userId)
	{		
		Profile userProfile = reader.getUserProfiles().get(userId);
		return getRecommendationsFromScores(userProfile,scores);
	}

}


