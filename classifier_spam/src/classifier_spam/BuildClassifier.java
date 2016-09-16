package classifier_spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

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
		if (args.length < 4) {
			throw new NullPointerException("Parameters not valid.");
		}

		// Ler os arquivos de treinamento e teste
		BufferedReader reader_tr = null;
		reader_tr = new BufferedReader(new FileReader(args[0]));
		BufferedReader reader_te = null;
		reader_te = new BufferedReader(new FileReader(args[1]));

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

		switch (args[2].toLowerCase()) {
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

		PrintWriter writer = new PrintWriter(args[3]);
		writer.println("Atual;Pred;Acerto");

		// Realiza as predições
		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = classifier.classifyInstance(testSet.instance(i));

			writer.print(testSet.instance(i).toString(testSet.classIndex()));
			writer.print(";");
			writer.print(testSet.classAttribute().value((int) pred));
			writer.print(";");
			if (pred == testSet.instance(i).classValue()) {
				writer.println("Sim");
			} else {
				writer.println("Não");
			}

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
		
		writer.close();		
		System.out.println("DONE!");
	}
}
