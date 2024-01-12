package alg.ib;

import util.reader.DatasetReader;
import similarity.metric.item.ItemSimilarityLearner;
import org.apache.commons.math3.linear.*;



public class Ease extends ItemSimilarityLearner {

	protected double lambda;

	public Ease(DatasetReader reader)
	{
		super(reader);
		this.lambda = 1.0;
		learnItemSimilarity();
	}
	public Ease(DatasetReader reader, double lambda)
	{
		super(reader);
		this.lambda = lambda;
		learnItemSimilarity();

	}

	@Override
	public void learnItemSimilarity() {
		int n = R.getColumnDimension();
		B = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				B[i][j] = 0.0;

		// Convert the rating matrix R to a RealMatrix object
		RealMatrix X = MatrixUtils.createRealMatrix(R.getData());

		// Create an identity matrix of size n for the regularisation term
		RealMatrix W = MatrixUtils.createRealIdentityMatrix(n);
		// Create another identity matrix of size n for later use
		RealMatrix D = MatrixUtils.createRealIdentityMatrix(n);
		// Compute the item-item co-occurrence matrix
		RealMatrix Y = X.transpose().multiply(X);
		// Multiply the co-occurrence matrix by the regularisation parameter lambda
		RealMatrix Z = Y.scalarMultiply(lambda);

		// Add the regularisation matrix and the co-occurrence matrix together
		RealMatrix M = Z.add(W);
		// Perform singular value decomposition on the resulting matrix
		SingularValueDecomposition svd = new SingularValueDecomposition(M);
		// Get the left singular vectors (U matrix) and the singular values (S matrix)
		RealMatrix U = svd.getU();
		RealMatrix S = svd.getS();

		// Calculate the similarity between each pair of items
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// Skip calculating similarity between the same item
				if (i == j) {
					continue;
				}
				// Initialize similarity to 0.0
				double sim = 0.0;
				// Calculate similarity using the U and S matrices
				for (int k = 0; k < S.getColumnDimension(); k++) {
					double sk = S.getEntry(k, k);
					double uk1 = U.getEntry(i, k);
					double uk2 = U.getEntry(j, k);
					sim += sk * uk1 * uk2;
				}
				// Normalize the similarity score using the trace of the S matrix
				B[i][j] = sim / (S.getTrace() - S.getEntry(0, 0));
			}

		}
	}


}