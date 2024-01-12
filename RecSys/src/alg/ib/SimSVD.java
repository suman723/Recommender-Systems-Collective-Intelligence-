package alg.ib;

import util.reader.DatasetReader;
import similarity.metric.item.ItemSimilarityLearner;

import java.util.Arrays;

import org.apache.commons.math3.linear.*;



public class SimSVD extends ItemSimilarityLearner {

	protected int K;
	protected double mu;

	public SimSVD(DatasetReader reader)
	{
		super(reader);
		this.K = 5;
		this.mu = 0.0;
		learnItemSimilarity();
	}
	public SimSVD(DatasetReader reader, int K)
	{
		super(reader);
		this.K = K;
		this.mu = 0.0;
		learnItemSimilarity();

	}
	public SimSVD(DatasetReader reader, int K,double mu)
	{
		super(reader);
		this.K = K;
		this.mu = mu;
		learnItemSimilarity();

	}

	@Override
	public void learnItemSimilarity() {
		// Get the number of items in the dataset
		int n = R.getColumnDimension();
		// Initialize matrix B with zeros
		B = new double[n][n];
		
		// Compute SVD of M
		SingularValueDecomposition svd = new SingularValueDecomposition(R);
		// Extract matrices U, S, Vt from the SVD
		RealMatrix U_matrix = svd.getU();
		// subsample the S matrix to map to a smaller latent dimension K
		RealMatrix S_matrix = MatrixUtils.createRealDiagonalMatrix(svd.getS().getSubMatrix(0, K - 1, 0, K - 1).getColumn(0));
		RealMatrix Ut_matrix = U_matrix.getSubMatrix(0, U_matrix.getRowDimension() - 1, 0, K - 1).transpose();
		// get the V matrix from SVD
		RealMatrix V_matrix = svd.getVT();

		RealMatrix Vt_matrix = V_matrix.getSubMatrix(0, K-1, 0 , V_matrix.getColumnDimension() - 1);

		
		RealMatrix G_matrix = S_matrix.multiply(Ut_matrix);
		// calculate item item similarity
		RealMatrix T_matrix = Vt_matrix.transpose().multiply(G_matrix);
		RealMatrix S2_matrix = T_matrix.multiply(T_matrix.transpose());

		// Set the values of matrix B based on the values of matrix S2
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				B[i][j] = B[j][i] = S2_matrix.getEntry(i, j);
			}
		}
	}
}


