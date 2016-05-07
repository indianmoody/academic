# I am using beginning and end of sentence symbols for Bigrams in this assignment.

import sys
import re               # Used to calculate number of lines in training file, which is useful in calculating probability for first word of sentence in Bigrams
import math

Unigrams = {}           # Dictionary to store unigrams of training file
Bigrams = {}            # Dictionary to store bigrams of training file
Unigrams2 = {}          # Dictionary to store unigrams of test file
Bigrams2 = {}           # Dictionary to store bigrams of test file

g = open(sys.argv[1])           #opening training file to get the count of number of lines/ sentences in training file
sen = re.compile('\\n+')
mLines = sen.findall(g.read())  #"mLines" is number f lines/sentences in training file
g.close()


#----------------------------- DETERMINATION OF UNIGRAMS AND BIGRAMS IN TRAINING FILE ---------------------------------------------------------------------
f = open(sys.argv[1])               # Opening traiing file

for line in f:                      #reading file line by line
    line = line.rstrip()
    tokens = line.split()           #splitting line into words

    lastword = "<s>"                #Beginning of sentence symbol, will be in bigram with first word of sentence


    for word in tokens:             #reading the line word by word
        word = word.lower()         #to make unigrams case-insensitive

        if word in Unigrams:        #Creating Unigram Model for training file
            Unigrams[word] += 1     #
        else:                       #
            Unigrams[word] = 1      #

        bigram = lastword + ' ' + word  #Creating bigram Model for training file
        if bigram in Bigrams:           #
            Bigrams[bigram] += 1        #
        else:                           #
            Bigrams[bigram] = 1         #
        lastword = word                 #making current word as previous word for next bigram

    word = "</s>"                       # End of sentence symbol
    bigram = lastword + ' ' + word      #Including end of sentence in bigram Model of training file
    if bigram in Bigrams:               #
        Bigrams[bigram] += 1            #
    else:                               #
        Bigrams[bigram] = 1             #

f.close()

# ------------------------------------ PROBABILITY FUNCTIONS -----------------------------------------------------------------------------

#probability of a word in unigram
def Prob1(words):
    return (Unigrams[words])/sum(Unigrams.values())


#probability of a word in bigram
def Prob2(words, previousWord):
    if previousWord == "<s>":                       #Case for first word of sentence:
        if words in Bigrams:                        #If the first word of a sentence in test file was starting a sentence in training file then
            return (Bigrams[words])/len(mLines)     #its probability is given by this formula
        else:                                       #otherwise.
            return 0                                #it is zero

    else:                                                   #Case for rest of the words of sentence:
        if words in Bigrams:
            return (Bigrams[words])/Unigrams[previousWord]  #Formula for probability of test file bigram also happen to exist in training file
        else:
            return 0

#probability of a bigram with one smooth
def Prob3(words, previousWord):
    if previousWord == "<s>":
        if words in Bigrams:
            return (Bigrams[words] + 1)/(len(mLines) + len(Unigrams))   #Modified formula probability for first word of sentence
        else:
            return 1/(len(mLines) + len(Unigrams))                      #Modified formula probability for first word of sentence that was not strarting word of a sentence in training file

    else:
        if words in Bigrams:
            return (Bigrams[words] + 1)/(Unigrams[previousWord] + len(Unigrams))    #Modified formula
        else:
            return 1/(Unigrams[previousWord] + len(Unigrams))                       #Modified Formula


# ----------------------------------------CREATING UNIGRAMS AND BIGRAMS FOR TEST FILE AND CALCULATING PROBABILITY OF SENTENCES------------------------
f2 = open(sys.argv[2])              #opening test file

for line2 in f2:                    #reading test file line by line
    line2 = line2.rstrip()
    tokens2 = line2.split()
    lastword = "<s>"                #including starting of sentence symbol for bigrams

    P1 = 1                          #Probability of sentence using unigram model
    P2 = 1                          #Probability of sentence using bigram model
    P3 = 1                          #Probability of sentence using bigram model with add-one smoothing

    for word2 in tokens2:
        word2 = word2.lower()
        if word2 in Unigrams2:
            Unigrams2[word2] += 1
        else:
            Unigrams2[word2] = 1

        P1 = Prob1(word2) * P1                  #Modifying probability of sentence (Unigram Model) by probability of word by word

        bigram2 = lastword + ' ' + word2
        if bigram2 in Bigrams2:
            Bigrams2[bigram2] += 1
        else:
            Bigrams2[bigram2] = 1

        P2 = Prob2(bigram2, lastword) * P2      #Modifying probability of sentence (Bigram Model) by probability of word by word
        P3 = Prob3(bigram2, lastword) * P3      #Modifying probability of sentence (Bigram Model with add-one smoothing) by porbability of word by word

        lastword = word2

    word2 = "</s>"
    bigram2 = lastword + ' ' + word2
    if bigram2 in Bigrams2:
        Bigrams2[bigram2] += 1
    else:
        Bigrams2[bigram2] = 1

    P2 = Prob2(bigram2, lastword) * P2          #Modifying probability of sentence (Bigram Model) by probability of last bigram of sentence
    P3 = Prob3(bigram2, lastword) * P3          #Modifying probability of sentence (Bigram Model with add-one smoothing) by probability of last bigram of sentence


    print("S = " + line2)                                               #Printing sentence
    print("Unigrams: logprob(S) = " + "%0.4f" % math.log10(P1))         #Printing log of probabiity of sentence by Unigram Model
    if P2 == 0:
        print("Bigrams: logprob(S) = Undefined")                        #If probability by Bigrm model is 0, print 'Undefined'
    else:
        print("Bigrams: logprob(S) = "+ "%0.4f" % math.log10(P2))       #Printing log of probabiity of sentence by Bigram Model
    print("Smoothed Bigrams: logprob(S) = "+ "%0.4f" % math.log10(P3))  #Printing log of probabiity of sentence by Bigram Model with add-one smoothing
    print("\n")

f2.close()

print(Unigrams)         #Printing dictionary containing Unigrams of training file
print(Bigrams)          #Printing dictionary containing Bigrams of training file