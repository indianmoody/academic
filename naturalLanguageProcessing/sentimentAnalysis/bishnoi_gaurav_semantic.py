'''
Author: Gaurav Bishnoi
CSCI 5832: Programming Assignment 4
'''


import random
import nltk
from nltk.corpus import movie_reviews

documents = [(list(movie_reviews.words(fileid)), category)
             for category in movie_reviews.categories()
             for fileid in movie_reviews.fileids(category)]
random.shuffle(documents)


#Introducing new word list: 'word_features'

list1 = []
fPos = open("posi.txt")
for line in fPos:
    line = line.strip()
    list1.append(line)

list2 = []
fPos = open("nega.txt")
for line in fPos:
    line = line.strip()
    list2.append(line)

word_features = list1 + list2
listLength = len(word_features)

print(len(word_features))


def document_features(document): # [_document-classify-extractor]
    document_words = set(document) # [_document-classify-set]
    features = {}
    for word in word_features:
        features['contains({})'.format(word)] = (word in document_words)
    return features

featuresets = [(document_features(d), c) for (d,c) in documents]

netAcc = 0      #variable for net accuracy

train_set, test_set = featuresets[200:], featuresets[:200]
classifier = nltk.NaiveBayesClassifier.train(train_set)
acc = nltk.classify.accuracy(classifier, test_set)      #accuracy of a fold of test_set
netAcc = netAcc + acc
print("Accuracy for " + str(1) + " fold: " + str(netAcc))


for foldVar in range(2, 11):
    train_set, test_set = featuresets[:200*(foldVar-1)] + featuresets[200*foldVar:], featuresets[200*(foldVar-1):200*foldVar]
    classifier = nltk.NaiveBayesClassifier.train(train_set)
    acc = nltk.classify.accuracy(classifier, test_set)
    netAcc = netAcc + acc
    print("Accuracy for " + str(foldVar) + " fold: " + str(acc))

#classifier = nltk.MaxentClassifier.train(train_set)
#classifier.show_most_informative_features(5)

print("Net Accuracy: " + str(netAcc/10))



# Extra Code 1
''' Code for list available in NTLK Book. Most of the unneccesary words and punctuations are removed from features list using 'Stopwords'

import random
import nltk
from nltk.corpus import movie_reviews
from nltk.corpus import stopwords
from nltk.classify import MaxentClassifier

import string

stop = stopwords.words('english')


documents = [(list(movie_reviews.words(fileid)), category)
             for category in movie_reviews.categories()
             for fileid in movie_reviews.fileids(category)]
random.shuffle(documents)

#Removing futile words from list
all_words = nltk.FreqDist(w.lower() for w in movie_reviews.words() if w.lower() not in stop and w.lower() not in string.punctuation)

#List without removing futile words
#all_words = nltk.FreqDist(w.lower() for w in movie_reviews.words())

word_features = list(all_words)[:2000] # [_document-classify-all-words]

def document_features(document): # [_document-classify-extractor]
    document_words = set(document) # [_document-classify-set]
    features = {}
    for word in word_features:
        features['contains({})'.format(word)] = (word in document_words)
    return features

featuresets = [(document_features(d), c) for (d,c) in documents]

netAcc = 0

train_set, test_set = featuresets[200:], featuresets[:200]
classifier = nltk.NaiveBayesClassifier.train(train_set)
acc = nltk.classify.accuracy(classifier, test_set)
netAcc = netAcc + acc
print("Accuracy for " + str(1) + " fold: " + str(netAcc))


for foldVar in range(2, 11):
    train_set, test_set = featuresets[:200*(foldVar-1)] + featuresets[200*foldVar:], featuresets[200*(foldVar-1):200*foldVar]
    classifier = nltk.NaiveBayesClassifier.train(train_set)
    acc = nltk.classify.accuracy(classifier, test_set)
    netAcc = netAcc + acc
    print("Accuracy for " + str(foldVar) + " fold: " + str(acc))

#classifier = nltk.MaxentClassifier.train(train_set)
#classifier.show_most_informative_features(5)
print("Net Accuracy: " + str(netAcc/10))

'''



#Extra Code 2
''' Code for Maximum Entropy Classifier

import random
import nltk
from nltk.corpus import movie_reviews
from nltk.corpus import stopwords
from nltk.classify import MaxentClassifier

import string

stop = stopwords.words('english')


documents = [(list(movie_reviews.words(fileid)), category)
             for category in movie_reviews.categories()
             for fileid in movie_reviews.fileids(category)]
random.shuffle(documents)

#Removing futile words from list
all_words = nltk.FreqDist(w.lower() for w in movie_reviews.words() if w.lower() not in stop and w.lower() not in string.punctuation)

#List without removing futile words
#all_words = nltk.FreqDist(w.lower() for w in movie_reviews.words())

word_features = list(all_words)[:200] # [_document-classify-all-words]

def document_features(document): # [_document-classify-extractor]
    document_words = set(document) # [_document-classify-set]
    features = {}
    for word in word_features:
        features['contains({})'.format(word)] = (word in document_words)
    return features

featuresets = [(document_features(d), c) for (d,c) in documents]

netAcc = 0

train_set, test_set = featuresets[200:], featuresets[:200]
classifier = nltk.MaxentClassifier.train(train_set)
acc = nltk.classify.accuracy(classifier, test_set)
netAcc = netAcc + acc
print("Accuracy for " + str(1) + " fold: " + str(netAcc))


for foldVar in range(2, 11):
    train_set, test_set = featuresets[:200*(foldVar-1)] + featuresets[200*foldVar:], featuresets[200*(foldVar-1):200*foldVar]
    classifier = nltk.MaxentClassifier.train(train_set)
    acc = nltk.classify.accuracy(classifier, test_set)
    netAcc = netAcc + acc
    print("Accuracy for " + str(foldVar) + " fold: " + str(acc))

#classifier = nltk.MaxentClassifier.train(train_set)
#classifier.show_most_informative_features(5)
#print("Net Accuracy: " + str(netAcc/10))

'''
