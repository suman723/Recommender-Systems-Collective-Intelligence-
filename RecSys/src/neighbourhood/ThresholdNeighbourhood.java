/**
 * A class that implements the "threshold neighbourhood" technique (user-based CF).
 */

package neighbourhood;
import java.util.HashSet;

import profile.Profile;
import similarity.SimilarityMap;


public class ThresholdNeighbourhood<T> extends Neighbourhood<T>
{
	private final double threshold; // the number of neighbours in the neighbourhood
	
	/**
	 * constructor - creates a new ThresholdNeighbourhood object
	 * @param k - the number of neighbours in the neighbourhood
	 */
	public ThresholdNeighbourhood(final double threshold)
	{
		super();
		this.threshold = threshold;
	}
	/**
	 * stores the neighbourhoods for all ids in member Neighbour.neighbourhoodMap
	 * @param simMap - a map containing user-user similarities
	 */
	public void computeNeighbourhoods(final SimilarityMap<T> simMap)
	{
		
		// Loop over each user id in the similarity map
		for (Integer userID: simMap.getIds()) {
			// Get the profile of the user id
			Profile user_profile = simMap.getSimilarities(userID);
			// Loop over each item id in the user's profile
			for (Integer itemID: user_profile.getIds()) {
				// Skip the current user id
				if (userID.equals(itemID)) {
					continue;
				}
				// Get the similarity score between the current user and the other user
				double similarity_score = user_profile.getValue(itemID);
				// see it its greater than the threshold and add it to the map
				if (similarity_score >= this.threshold) {
				this.add(userID, itemID);
				}
				
			}}}}
		
