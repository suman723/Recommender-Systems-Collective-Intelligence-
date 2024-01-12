package alg.ub.predictor;
import java.util.Set;
import profile.Profile;

import alg.ub.UBCFRatingPredictionAlg;


public class DeviationFromUserMeanPredictor implements UBCFPredictor 
{
	/**
	 * constructor - creates a new DeviationFromMeanUserPredictor object
	 */


	public DeviationFromUserMeanPredictor()
	{
	}

	/**
	 * @returns the target user's predicted rating for the target item or null if a prediction cannot be computed
	 * @param alg - UserBasedCF algorithm
	 * @param userId - the numeric ID of the target user
	 * @param itemId - the numerid ID of the target item
	 */

	/**

	Returns the predicted rating for the target item by the target user,

	or null if a prediction cannot be computed

	@param algorithm - the UserBasedCF algorithm

	@param userID - the numeric ID of the target user

	@param itemID - the numeric ID of the target item

	@return the predicted rating for the target item by the target user
	 */
	public Double getPrediction(final UBCFRatingPredictionAlg algorithm, final Integer userID, final Integer itemID) {

		// Get the set of neighbour IDs of the target user
		Set<Integer> neighbourIDs = algorithm.getNeighbourhood().getNeighbours(userID);

		// Return 0.0 if there are no neighbours for the target user
		if (neighbourIDs == null || neighbourIDs.isEmpty()) {
			return 0.0;
		}

		// Get the target user profile and calculate their mean rating
		Profile targetUser = algorithm.getReader().getUserProfiles().get(userID);
		double targetUserMeanRating = targetUser.getMeanValue();

		// Initialize variables to calculate the sum of deviations of the target user's neighbours
		double sumOfDeviations = 0.0;
		int deviationCount = 0;

		// Iterate over the neighbours of the target user and calculate the deviation of each neighbour from their mean rating
		for (Integer neighbourID : neighbourIDs) {
			Profile neighbour = algorithm.getReader().getUserProfiles().get(neighbourID);
			Double neighbourRating = neighbour.getValue(itemID);
			if (neighbourRating != null) {
				double deviation = neighbourRating - neighbour.getMeanValue();
				sumOfDeviations += deviation;
				deviationCount++;
			}
		}

		// Return null if no deviation was calculated
		if (deviationCount == 0) {
			return null;
		}

		// Get the target item profile and calculate its mean rating
		Profile targetItem = algorithm.getReader().getItemProfiles().get(itemID);
		double targetItemMeanRating = targetItem.getMeanValue();

		// Calculate the deviation of the target user from their mean rating
		double deviation = sumOfDeviations / deviationCount;

		// Calculate the predicted rating for the target item by the target user
		double predictedRating = targetUserMeanRating + deviation;

		// Adjust the predicted rating 
		predictedRating = Math.min(predictedRating, 5.0);
		predictedRating = Math.max(predictedRating, 1.0);

		return predictedRating;
	}






}


