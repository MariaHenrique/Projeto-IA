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
public class BuildClassifier {

	public static void main(String[] args) throws Exception {

		// args = fileTrain fileTest classificador outputFile.csv
		if (args.length != 5 && args.length != 2 && args.length != 4) {
			throw new NullPointerException("Parameters not valid.");
		}
		if (args[0].equals("1")){
			createClassifierModel(args[1], args[2], args[3],args[4]);
		} if (args[0].equals("2")){
			createClassifierModelCV(args[1], args[2], args[3]);
		}else {
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
	private static void createClassifierModel(String train_file, String test_file, String algotithm, String output_file) throws Exception{
		// Ler os arquivos de treinamento e teste
				BufferedReader reader_tr = null;
				reader_tr = new BufferedReader(new FileReader(train_file));
				BufferedReader reader_te = null;
				reader_te = new BufferedReader(new FileReader(test_file));

				// Carrega os dados de treinamento e teste
				Instances trainSet = new Instances(reader_tr);
				Instances testSet = new Instances(reader_te);

				// Pega a classe de cada linha do arquivo (que [e o último atributo)
				trainSet.setClassIndex(trainSet.numAttributes() - 1);
				testSet.setClassIndex(testSet.numAttributes() - 1);
				// Fecha os buffers
				reader_tr.close();
				reader_te.close();

				// Verifica se o arquivo de treino e teste possui os mesmos atributos
				if (!trainSet.equalHeaders(testSet))
					throw new IllegalArgumentException(
							"Train and test set are not compatible: " + trainSet.equalHeadersMsg(testSet));

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
				classifier.buildClassifier(trainSet);
				
			    weka.core.SerializationHelper.write("files/cls.model", classifier);

				PrintWriter writer = new PrintWriter("files/" + output_file);
				//writer.println("Atual;Pred;Acerto");

				// Realiza as predições
				for (int i = 0; i < testSet.numInstances(); i++) {
					double pred = classifier.classifyInstance(testSet.instance(i));

				/*	writer.print(testSet.instance(i).toString(testSet.classIndex()));
					writer.print(";");
					writer.print(testSet.classAttribute().value((int) pred));
					writer.print(";");
					if (pred == testSet.instance(i).classValue()) {
						writer.println("Sim");
					} else {
						writer.println("Não");
					}*/

				}
				
				// Avaliação
				Evaluation evaluation = new Evaluation(trainSet);
				evaluation.evaluateModel(classifier, testSet);
				System.out.println();
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
	private static void createClassifierModelCV(String m_file, String algotithm, String output_file) throws Exception{
		BufferedReader reader_file = null;
		reader_file = new BufferedReader(new FileReader(m_file));
		
		Instances m_set = new Instances(reader_file);
		
		m_set.setClassIndex(m_set.numAttributes() - 1);

		reader_file.close();

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
		
		classifier.buildClassifier(m_set);
		
	   // weka.core.SerializationHelper.write("files/cls_cv.model", classifier);
	    
	    Evaluation evaluation = new Evaluation(m_set);
	    evaluation.crossValidateModel(classifier, m_set, 10, new Random(1));			    
    		    
	    PrintWriter writer = new PrintWriter("files/" + output_file);
	    writer.println("F-Measure (HAM);Precisão (HAM);Recuperação (HAM);F-Measure (SPAM);Precisão (SPAM);Recuperação (SPAM); Acurácia");
		writer.print(evaluation.fMeasure(0)+";");
		writer.print(evaluation.precision(0)+";");
		writer.print(evaluation.recall(0)+";");
		writer.print(evaluation.fMeasure(1)+";");
		writer.print(evaluation.precision(1)+";"); 
		writer.print(evaluation.recall(1)+";");
		System.out.println("Estimated Accuracy: "+Double.toString(evaluation.pctCorrect()));
		
		
		double[][] cm = evaluation.confusionMatrix();
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
