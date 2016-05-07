#Author: Gaurav Bishnoi
#Programming Assignment 3 in progress
#Created necessary unigrams and bigrams for tags and words required for viterbi.

import sys
import re
import math

uniWords = {}
uniTag = {}
biTag = {}
biTagWords = {}

#f = open(sys.argv[1])           #opening training file
g = open(sys.argv[1])           #opening training file to get the count of number of lines/ sentences in training file
sen = re.compile('\\n\\n+')     #will be used to divide file into sentences
mLines = sen.findall(g.read())  #"mLines" is number f lines/sentences in training file
print(len(mLines))
g.close()

readingCount = 0
count = 0
f = open(sys.argv[1])
#content = sen.split(f.read())       #splitting into sentences
for line in sen.split(f.read()):                #reading sentences
    if readingCount >= 0.8 * len(mLines):       #making 80% file as training file
        break
    readingCount += 1
    lastTag = "<s>"
    if len(line) != 0:
        for lineSen in line.split("\n"):        #breaking sentence into lines
            twoWords = lineSen
            token = lineSen.split('\t')       #breaking line of sentence into word and tag
            if len(token) == 2:
                word = token[0]
                tag = token[1]
                #print(twoWords)

                #Section for unigrams and bigrams
            #biTagWords
            if twoWords in biTagWords:
                biTagWords[twoWords] += 1
            else:
                biTagWords[twoWords] = 1

            #uniWords
            if word in uniWords:
                uniWords[word] += 1
            else:
                uniWords[word] = 1

            #uniTag
            if tag in uniTag:
                uniTag[tag] += 1
            else:
                uniTag[tag] = 1

            #biTag
            doubleTag = lastTag + "\t" + tag
            if doubleTag in biTag:
                biTag[doubleTag] += 1
            else:
                biTag[doubleTag] = 1
            lastTag = tag






f.close()
print(biTag)
print(readingCount)