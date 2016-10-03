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
import math

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
#    print (text)
#    raw_input("Digite um caracter")
    if setting=='bow':
        return {word: count for word, count in Counter(preprocess(text)).items() if not word in stoplist}
    else:
        return {word: True for word in preprocess(text) if not word in stoplist}

args = sys.argv

if(len(args) != 4):
	print ("python extract-words-more-frequent-SPAMS <PATH/TO/EMAILS-TRAIN-SPAM/> <NUMBER-WORDS-MORE-FREQUENT> <FILENAME-CVS>")
else:
	path_train_spam = args[1]
	n = int(args[2])
	filename_out = args[3]
	#path_train_ham = args[2]

	spam = init_lists(path_train_spam)
	

	all_emails = [(email, 'spam') for email in spam]
	
	print ('Corpus size = ' + str(len(all_emails)) + ' emails')

	all_features = [(get_features(email, 'bow'), label) for (email, label) in all_emails]
	
	
	
	words = []
	frequency = []
	print ("computing frequency of words...")
	for email_feat in all_features:
		for word in email_feat[0].keys():
			if (word in words):
				frequency[words.index(word)] = frequency[words.index(word)] + 1
			else:
				words.append(word)
				frequency.append(email_feat[0][word])


	print ("sorting the frequency...")
	flag = True
	while(flag):
		flag = False
		for i in range(len(frequency)-1,0,-1):
			if(frequency[i] > frequency[i-1]):
				aux = words[i]
				words[i] = words[i-1]
				words[i-1] = aux
			
				aux = frequency[i]
				frequency[i] = frequency[i-1]
				frequency[i-1] = aux
			
				flag = True
	index = 0
	list_words_more_frenquent = []
	count = 0
	while(count < n):
		if(len(words[index]) <= 3 or words[index] == "subject" or words[index]=="http"):
			index+=1
			continue
		list_words_more_frenquent.append(words[index])
		index += 1	
		count +=1
	
	myfile = open(filename_out, 'wb')
	wr = csv.writer(myfile, quoting=csv.QUOTE_ALL)
	wr.writerow(list_words_more_frenquent)

	df = [0 for i in range(n)]
	
	for email in spam:
		for word in list_words_more_frenquent:
			if(str(word) in preprocess(email)):
				df[list_words_more_frenquent.index(word)]+=1
					
	logs = []
	n = len(spam)
	for i in df:
		logs.append(math.log10( n / (float(i)) ) )
	
	l = [list_words_more_frenquent, logs]

	with open("words_and_log.txt", "wb") as f:
    		writer = csv.writer(f)
    		writer.writerows(l)
	
	
	myfile2 = open("df-test.txt", 'wb')
	wr2 = csv.writer(myfile2, quoting=csv.QUOTE_ALL)
	wr2.writerow(df)
	
	myfile3 = open("logs-test.txt", 'wb')
	wr3 = csv.writer(myfile3, quoting=csv.QUOTE_ALL)
	wr3.writerow(logs)
	
	print ('Collected ' + str(len(all_features)) + ' feature sets')
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
