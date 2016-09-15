package classifier_spam;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.Instances;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.Evaluation;

public class BuildClassifier {
	
	public static void main(String[] args) throws Exception{
		
		//Ler os arquivos de treinamento e teste
		BufferedReader reader_tr = null;
		reader_tr = new BufferedReader(new FileReader("C:/Users/Daniela/Documents/CC/OitavoPer�odo/IA/classifier_spam/src/classifier_spam/train.arff"));
		BufferedReader reader_te = null;
		reader_te = new BufferedReader(new FileReader("C:/Users/Daniela/Documents/CC/OitavoPer�odo/IA/classifier_spam/src/classifier_spam/test.arff"));
		
		//Carrega os dados de treinamento e teste
	    Instances trainSet = new Instances(reader_tr);
	    Instances testSet = new Instances(reader_te);
	    
	    //Pega a classe de cada linha do arquivo (que [e o �ltimo atributo)
	    trainSet.setClassIndex(trainSet.numAttributes() - 1);
	    testSet.setClassIndex(testSet.numAttributes() - 1);
	    //Fecha os buffers
	    reader_tr.close();
	    reader_te.close();
	    
	    //Verifica se o arquivo de treino e teste possui os mesmos atributos
	    if (!trainSet.equalHeaders(testSet))
	      throw new IllegalArgumentException(
		  "Train and test set are not compatible: " + trainSet.equalHeadersMsg(testSet));
	    
	    //Constr�i o classificador J48 com o conjunto de treinamento
	    J48 cls1 = new J48();
	    cls1.buildClassifier(trainSet);
	    
	  //Constr�i o classificador NaiveBayes com o conjunto de treinamento
	    NaiveBayes cls2 = new NaiveBayes();
	    cls2.buildClassifier(trainSet);
	    
	    //Constr�i o classificador SMO com o conjunto de treinamento
	    SMO cls3 = new SMO();
	    cls3.buildClassifier(trainSet);
	      
	    //Realiza as predi��es
	    System.out.println("ID- Atual - Pred - Acerto");
	    for (int i = 0; i < testSet.numInstances(); i++) {
	      double pred = cls1.classifyInstance(testSet.instance(i));
	      
	      System.out.print((i+1));
	      System.out.print(" - ");
	      System.out.print(testSet.instance(i).toString(testSet.classIndex()));
	      System.out.print(" -  ");
	      System.out.print(testSet.classAttribute().value((int) pred));
	      System.out.print(" -  ");
	      if (pred == testSet.instance(i).classValue()){
	    	  System.out.print("Sim");
	      } else {
	    	  System.out.print("N�o");
	      }
	      System.out.println();
	    }
	    //Avalia��o 
	    Evaluation evaluation = new Evaluation(trainSet);
	    evaluation.evaluateModel(cls1, testSet); 
	    System.out.println();
		System.out.println("F-Measure (HAM): " + evaluation.fMeasure(0) +  " - Precis�o (HAM): " + evaluation.precision(0) + " - Recupera��o (HAM): " + evaluation.recall(0));
		System.out.println("F-Measure (SPAM): " + evaluation.fMeasure(1) +  " - Precis�o (SPAM): " + evaluation.precision(1) + " - Recupera��o (SPAM): " + evaluation.recall(1));
		
	  }
}
