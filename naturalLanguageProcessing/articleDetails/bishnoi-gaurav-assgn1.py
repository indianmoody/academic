import sys
import re

helloFile = open(sys.argv[1])
helloContent = helloFile.read()

# words
word1 = re.compile(' |\\.\\n|!\\n|\\?\\n|\\"\\n')
nWords = word1.findall(helloContent)
print("Number of words in file " + sys.argv[1] + " are: " + str(len(nWords)))


#sentences
sen1 = re.compile('\\. +[A-Z]|\\. \\"|\\.\\n|\\.\\"\\n|\\.\\"|\\? [A-Z]|\\?\\n|[a-zA-Z]!+|[a-zA-Z]!+\\n|\\.\\) +|\\.\\)\\n')
nLines = sen1.findall(helloContent)
negSen1 = re.compile("Mr\\.| mr\\.| mrs\\.|Mrs\\.|Dr\\.| dr\\.| [A-Z]\\. |Jr\\. | jr\\. |Sr\\. | sr\\. |St\\. | st\\. ")
negLines1 = negSen1.findall(helloContent)
print("Number of sentences in file " + sys.argv[1] + " are: " + str(len(nLines) - len(negLines1)))


#paragraphs
para1 = re.compile('[^\\r\\n]+((\\r|\\n|\\r\\n)[^\\r\\n]+)*')
nPara = para1.findall(helloContent)
print("Number of paragraphs in file " + sys.argv[1] + " are: " + str(len(nPara)))
