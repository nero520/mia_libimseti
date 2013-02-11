This project enables the evaluation of:

-Absolute difference

-Precision

-Recall

-F1 

for Mahout's main similarity metrics:

-Euclidean distance

-Pearson correlation

-Tanimoto coefficient

-LogLikelihood

on user-based recommenders. It also runs some recommendation examples.

It was proven for libimseti.cz dating site's data: 

http://www.occamslab.com/petricek/data/libimseti-complete.zip

although it can fit many other databases as long as it fits DataModel object; that is, 
a csv with userid, objectid, rating format.

Please note this code is only for academic and research purposes, there is no commercial intention.

STEPS:
------

1. Download the database: http://www.occamslab.com/petricek/data/libimseti-complete.zip

2. Import this project in your favourite IDE as a Java project. 

3. IMPORTANT. Add Maven_dependencies_list.txt jars to the project's build path.
   You might as well create a system library with these Maven depencencies because it is often used,
   or place it under <dependency> tag in the pom.xml and import it as a maven project.
   
4. Open Evaluator.java and edit datafile as required to the path of your ratings.dat. 

5. Run Evaluator by passing two int parameters: the id of the user you would like to recommend 
   and how many recommendations you will like to give

6. There are several values in GenericEvaluator.java that can be tweaked, like:
7. 
   -Precision etc. calculation parameters in GenericRecommenderIRStatsEvaluator.evaluate() 

   -Training/testing percentages in AverageAbsoluteDifferenceRecommenderEvaluator.evaluate()
   
   -Neighboors in NearestNUserNeighborhood()	

