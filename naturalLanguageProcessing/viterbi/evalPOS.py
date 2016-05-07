import sys

if len(sys.argv)!=3:
	print('Usage: python evalPOS.py goldPOS_filename systemPOS_filename')
	sys.exit(1)

gold = [line.strip() for line in open(sys.argv[1], 'r')]
system = [line.strip() for line in open(sys.argv[2], 'r')]

if len(gold)!=len(system):
	print('Number of lines between gold and system do not match!')
	sys.exit(1)

tagCnt = 0
correctCnt = 0
for i in range(0,len(gold)):
	if not gold[i]:
		continue
	tagCnt+=1

	goldPOS = gold[i].split('\t')[1]

	if not system[i]:
		continue
	elif goldPOS != system[i].split('\t')[1]:
		continue
	correctCnt+=1

print('Accuracy: %f%% %d/%d correct POS tags' % (100.0*(correctCnt)/tagCnt, correctCnt, tagCnt))
