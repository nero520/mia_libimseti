package mia.recommender.ch05;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class GenericEvaluator {
	
	//Average absolute difference between expected and gotten. Depends on scale (1-5, 1-10,0-1...). 
    //In this case, between 1 and 10
	public double absolutedifference(DataModel model, final String sim) throws TasteException {

	    RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
	      @Override
	      public Recommender buildRecommender(DataModel model) throws TasteException {
	    	  
	    	  UserSimilarity similarity = null;
	      	  if (sim.equals("EuclideanDistanceSimilarity")){
	      	  	  similarity = new EuclideanDistanceSimilarity(model);
	      	  }else if (sim.equals("PearsonCorrelationSimilarity")){
	        	  similarity = new PearsonCorrelationSimilarity(model);
	      	  }else if (sim.equals("TanimotoCoefficientSimilarity")){
	      		  similarity = new TanimotoCoefficientSimilarity(model);
	      	  }else if (sim.equals("LogLikelihoodSimilarity")){
	      		  similarity = new LogLikelihoodSimilarity(model);
	      	  }  
	        UserNeighborhood neighborhood =  new NearestNUserNeighborhood(100, similarity, model);
	        return new GenericUserBasedRecommender(model, neighborhood, similarity);
	      }
	    };
		RecommenderEvaluator absoluteevaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteevaluator.evaluate(recommenderBuilder, null, model, 0.95, 0.05);
		return score;
	}

	//Precisson and Recall, P=tp/tp+fp, R=tp/tp+fn
	public IRStatistics precisionrecall(DataModel model) throws TasteException{
		
	   RecommenderIRStatsEvaluator PRevaluator = new GenericRecommenderIRStatsEvaluator();
	   RecommenderBuilder PRrecommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
			  UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
			  UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
			  return new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
			}
       };
       //evaluate(RecommenderBuilder recommenderBuilder, DataModelBuilder dataModelBuilder, DataModel dataModel, IDRescorer rescorer, 
       //int at, double relevanceThreshold, double evaluationPercentage) 
       //at - as in, "precision at 5". The number of recommendations to consider when evaluating precision, etc.
       //relevanceThreshold - items whose preference value is at least this value are considered "relevant" for the purposes of computations 
	   IRStatistics stats = PRevaluator.evaluate(PRrecommenderBuilder, null, model, null, 1000000, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
	   return stats;
	}
	
	//Simple recommendation list
	public List<RecommendedItem> recommend(DataModel model, String similarity, Long userid, int howmany) throws TasteException{
		UserSimilarity euclideansimilarity = new EuclideanDistanceSimilarity(model);
		UserNeighborhood euclideanneighborhood = new NearestNUserNeighborhood(100, euclideansimilarity, model);
		Recommender euclideanrecommender = new GenericUserBasedRecommender(model, euclideanneighborhood, euclideansimilarity);
	    List<RecommendedItem> recommendations = euclideanrecommender.recommend(Long.valueOf(userid), howmany);
	    return recommendations;
	}
	
}
