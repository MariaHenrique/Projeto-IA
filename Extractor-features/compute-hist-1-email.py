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
	l = [0 for i in range(n)]
	
	return l
	
def read_file_csv(file_name_csv):
	l = []
	with open(file_name_csv, 'rb') as f:
	    reader = csv.reader(f)
	    l = list(reader)
	return l
	

args = sys.argv

path_email = args[1]

f = open(path_email, 'r')

email = f.read()

f.close()

file_name_csv = args[2]
l_aux = read_file_csv(file_name_csv)
list_words = l_aux[0]
logs = l_aux[1]

n_words = int(args[3])

hist = zeros_list(n_words)

email = preprocess(email)

for i in range(n_words):
	hist[i] = (email.count(list_words[i]) ) * float(logs[i])

hist.append("email")

#gambiarra...
hists = [hist, hist]

with open('hist_email_demo.csv', "wb") as f:
	    writer = csv.writer(f)
	    wr = csv.writer(f, quoting=csv.QUOTE_ALL)
	    writer.writerows(hists)

c = 'python csv2arff.py hist_email_demo.csv'
os.system(c)
