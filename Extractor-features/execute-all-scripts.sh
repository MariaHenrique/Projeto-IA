#!/bin/bash

python extract-words-more-frequent-SPAMS.py "dataset/teste/spam/" "dataset/treino/spam/" "100" "words_frequen_in_spam.csv"

python extract-features.py "dataset/treino/ham/" "dataset/treino/spam/" "hist_treino.csv" "treino"

python extract-features.py "dataset/teste/ham/" "dataset/teste/spam/" "hist_teste.csv" "teste"

python csv2arff.py "hist_treino.csv"

python csv2arff.py "hist_teste.csv"
