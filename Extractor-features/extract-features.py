#coding:utf-8
#
# Project IA
# Group: Andreza Queiroz,
#	 Carlos Interaminense, 
#	 Maria Daniela
#
#Author: Carlos Interaminense

from __future__ import print_function, division
import nltk
import os
import random
from collections import Counter
from nltk import word_tokenize, WordNetLemmatizer
from nltk.corpus import stopwords
from nltk import NaiveBayesClassifier, classify
import sys
import csv

stoplist = stopwords.words('english')

#Função para carregar os emails
def init_lists(folder):
    a_list = []
    file_list = os.listdir(folder)
    for a_file in file_list:
        f = open(folder + a_file, 'r')
        a_list.append(f.read())
    f.close()
    return a_list
 
# Função para realizar um preprocessamento dos emails, retirando o plural das palavras por exemplo.
# Com isso, "examples" e "example" são consideradas as mesmas palavras.
def preprocess(sentence):
    lemmatizer = WordNetLemmatizer()
    return [lemmatizer.lemmatize(word.lower()) for word in word_tokenize(unicode(sentence, errors='ignore'))]

# Computa a frequencia das palavras dos emails. 
def get_features(text, setting):
    if setting=='bow':
        return {word: count for word, count in Counter(preprocess(text)).items() if not word in stoplist}
    else:
        return {word: True for word in preprocess(text) if not word in stoplist}

def zeros_list(n):
	l = []
	for i in range(n):
		l.append(0)

	return l
	
def read_file_csv(file_name_csv):
	l = []
	with open(file_name_csv, 'rb') as f:
	    reader = csv.reader(f)
	    l = list(reader)
	return l
	
args = sys.argv

if(len(args) != 5):
	print("python extract-features.py <PATH/TO/FOLDER/HAM> <PATH/TO/FOLDER/SPAM> <FILENAME-HISTOGRAMS> <(treino ou teste)>")
else:
	path_ham = args[1]
	path_spam = args[2]
	filename_out_histograms = args[3]
	is_train = ("treino" == args[4])

	file_name_csv = "words_frequen_in_spam.csv"
	list_words = read_file_csv(file_name_csv)[0]

	ham = init_lists(path_ham)
	spam = init_lists(path_spam)

	spam = [(email, 'spam') for email in spam]
	ham = [(email, 'ham') for email in ham]

	histograms = []
	for email in spam:
		hist_email = zeros_list(len(list_words))
		label = email[1]
		for i in range(len(list_words)):
			if(list_words[i] in email[0]):
				hist_email[i] = hist_email[i] +1;
		if(is_train): hist_email.append(label)
		else: hist_email.append("null")
		histograms.append(hist_email)
		

	for email in ham:
		hist_email = zeros_list(len(list_words))
		label = email[1]
		for i in range(len(list_words)):
			if(list_words[i] in email[0]):
				hist_email[i] = hist_email[i] +1;
		if(is_train): hist_email.append(label)
		else: hist_email.append("null")
		histograms.append(hist_email)
	
	random.shuffle(histograms)

	with open(filename_out_histograms, "wb") as f:
	    writer = csv.writer(f)
	    wr = csv.writer(f, quoting=csv.QUOTE_ALL)
	    writer.writerows(histograms)
