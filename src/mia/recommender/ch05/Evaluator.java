package mia.recommender.ch05;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class Evaluator {

	public static void main(String[] args) throws TasteException, IOException {
		
		double score;
		IRStatistics stats;
		List<RecommendedItem> recommendations;
		String[] sim = {"EuclideanDistanceSimilarity","PearsonCorrelationSimilarity","TanimotoCoefficientSimilarity","LogLikelihoodSimilarity"};
		String datafile = "ratings.dat";

		DataModel model = new FileDataModel(new File(datafile));
	    GenericEvaluator Genericevaluator = new GenericEvaluator();

		for (int i=0;i<sim.length;i++){
	
		    score = Genericevaluator.absolutedifference(model,sim[i]);
		    System.out.println("Absolute difference(1-10 scale): " + score +"\n");
	    
		    stats = Genericevaluator.precisionrecall(model);   
		    System.out.println("Precision: " + stats.getPrecision()+ ", Recall: " + stats.getRecall() + "\n");
		    
		    recommendations = Genericevaluator.recommend(model, sim[i], Long.valueOf(args[0]), Integer.parseInt(args[1]));
		    System.out.println("Recommendations\n" + recommendations + "\n");
		}
	}
}
