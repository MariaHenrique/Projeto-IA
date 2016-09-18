#coding:utf-8

# Project IA
# Group: Andreza Queiroz,
#	 Carlos Interaminense, 
#	 Maria Daniela

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

# Extrai as características dos emails. 
def get_features(text, setting):
    if setting=='bow':
        return {word: count for word, count in Counter(preprocess(text)).items() if not word in stoplist}
    else:
        return {word: True for word in preprocess(text) if not word in stoplist}

args = sys.argv

path_train_spam = args[1]
path_train_ham = args[2]

spam = init_lists(path_train_spam)
ham = init_lists(path_train_ham)

all_emails = [(email, 'spam') for email in spam]
all_emails += [(email, 'ham') for email in ham]

print ('Corpus size = ' + str(len(all_emails)) + ' emails')

'''
all_features = []
count = 1
for email, label in all_emails:
	print ('email. ' + str(count))
	print email
	count +=1
	all_features.append((get_features(email, ""), label))
	print all_features
'''
all_features = [(get_features(email, 'bow'), label) for (email, label) in all_emails]
words = []
frequency = []
for email_feat in all_features:
	for word in email_feat[0].keys():
		if (word in words):
			frequency[words.index(word)] = frequency[words.index(word)] + 1
		else:
			words.append(word)
			frequency.append(email_feat[0][word])

#	print(email_feat)
	print (words)
	print (frequency)
	raw_input()

print ('Collected ' + str(len(all_features)) + ' feature sets')
print (all_features[0])
print ("\n\n#################\n\n")
print (all_features[1])









 
 
''' 
def train(features, samples_proportion):
    train_size = int(len(features) * samples_proportion)
    # initialise the training and test sets
    train_set, test_set = features[:train_size], features[train_size:]
    print ('Training set size = ' + str(len(train_set)) + ' emails')
    print ('Test set size = ' + str(len(test_set)) + ' emails')
    # train the classifier
    classifier = NaiveBayesClassifier.train(train_set)
    return train_set, test_set, classifier
 
def evaluate(train_set, test_set, classifier):
    # check how the classifier performs on the training and test sets
    print ('Accuracy on the training set = ' + str(classify.accuracy(classifier, train_set)))
    print ('Accuracy of the test set = ' + str(classify.accuracy(classifier, test_set)))
    # check which words are most informative for the classifier
    classifier.show_most_informative_features(20)
 

# initialise the data
spam = init_lists('enron1/spam/')
ham = init_lists('enron1/ham/')
all_emails = [(email, 'spam') for email in spam]
all_emails += [(email, 'ham') for email in ham]
random.shuffle(all_emails)
print ('Corpus size = ' + str(len(all_emails)) + ' emails')
 
# extract the features
all_features = [(get_features(email, ''), label) for (email, label) in all_emails]
print ('Collected ' + str(len(all_features)) + ' feature sets')
 
# train the classifier
train_set, test_set, classifier = train(all_features, 0.8)
 
# evaluate its performance
evaluate(train_set, test_set, classifier)
'''













