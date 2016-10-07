package classifier_spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;

public class BuildClassifierCrossValidation {

	public static void main(String[] args) throws Exception {

		// args =  fileTrain fileTest classificador outputFile.csv
		if (args.length != 5 && args.length != 2) {
			throw new NullPointerException("Parameters not valid.");
		}
		
		if (args[0].equals("1")){
			createClassifierModel(args[1], args[2], args[3]);
		} else {
			testInst(args[1]);	
		}
		 
	}
	private static void testInst(String test_file) throws Exception{
		BufferedReader reader_te = null;
		reader_te = new BufferedReader(new FileReader(test_file));
		
		Instances testSet = new Instances(reader_te);
		testSet.setClassIndex(testSet.numAttributes() - 1);
		reader_te.close();
		
		Classifier classifier = (Classifier) weka.core.SerializationHelper.read("files/cls.model");
	
		//PrintWriter writer = new PrintWriter("files/" + output_file);
		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = classifier.classifyInstance(testSet.instance(i));
		
			if (pred == 0){
				System.out.println("E-mail HAM");
			} else {
				System.out.println("E-mail SPAM");
			}
	
		}
	}
		
	private static void createClassifierModel(String train_file, String algotithm, String output_file) throws Exception{
		// Ler os arquivos de treinamento e teste
				BufferedReader reader_tr = null;
				reader_tr = new BufferedReader(new FileReader(train_file));
				
				// Carrega os dados de treinamento e teste
				Instances trainSet = new Instances(reader_tr);
				
				// Pega a classe de cada linha do arquivo (que [e o último atributo)
				trainSet.setClassIndex(trainSet.numAttributes() - 1);

				// Fecha os buffers
				reader_tr.close();

				Classifier classifier = null;

				switch (algotithm.toLowerCase()) {
				case "j48":
					classifier = new J48();
					break;
				case "naivebayes":
					classifier = new NaiveBayes();
					break;
				case "smo":
					classifier = new SMO();
					break;
				case "radomtree":
					classifier = new RandomTree();
					break;
				case "naivebayesmultinomial":
					classifier = new NaiveBayesMultinomial();
					break;
				case "multilayerperceptron":
					classifier = new MultilayerPerceptron();
					break;
				default:
					break;
				}
				if (classifier == null) {
					throw new NullPointerException("Invalid Classifier in parameter.");
				}
				
				
				  //Training instances are held in "originalTrain"

			    
			    Evaluation evaluation = new Evaluation(trainSet);
			    evaluation.crossValidateModel(classifier, trainSet, 10, new Random(1));
			    
			    PrintWriter writer = new PrintWriter("files/" + output_file);
			    writer.println("F-Measure (HAM);Precisão (HAM);Recuperação (HAM);F-Measure (SPAM);Precisão (SPAM);Recuperação (SPAM)");
				writer.print(evaluation.fMeasure(0)+";");
				writer.print(evaluation.precision(0)+";");
				writer.print(evaluation.recall(0)+";");
				writer.print(evaluation.fMeasure(1)+";");
				writer.print(evaluation.precision(1)+";");
				writer.println(evaluation.recall(1));
				
				double[][] cm = evaluation.confusionMatrix();
				System.out.println();
				writer.println();
				writer.println("Matriz de confusão");
				writer.println("TP; FP; FN; TN");
				for (int i=0; i < cm.length; i++ ){
					for (int j=0; j < cm[0].length; j++){
						writer.print(cm[i][j] + ";");
					}
				}
				writer.close();		
				System.out.println("DONE!");
				
			
	}
}
