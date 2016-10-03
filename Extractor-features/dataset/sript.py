import sys
import os

argv = sys.argv

path_src = argv[1]
path_dst = argv[2]
n_file_to_move = int(argv[3])

filenames_emails = os.listdir(path_src)
count = 0
for email in filenames_emails:
	if(count == n_file_to_move): break
	command = "mv %s/%s %s" % (path_src, email, path_dst)
	print "%d. %s" % (count,command)
	count+=1
	os.system(command)
	
