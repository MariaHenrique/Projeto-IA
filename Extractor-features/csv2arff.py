###############
# CSV to ARFF #
###############
import csv
import sys
from time import sleep

class convert(object):

    content = []
    name = ''

    def __init__(self):
        self.csvInput()
        self.arffOutput()

    #import CSV
    def csvInput(self):

        user = sys.argv[1] #"file.csv"

        #remove .csv
        if user.endswith('.csv') == True:
            self.name = user.replace('.csv', '')
            
        try:
            with open(user, 'rb') as csvfile:
               lines = csv.reader(csvfile, delimiter = ',')
               for row in lines:
                   self.content.append(row)
            csvfile.close()
            sleep(2) #sleeps added for dramatic effect!
            
        #just in case user tries to open a file that doesn't exist
        except IOError:
            sleep(2)
            print 'File not found.\n'
            self.csvInput()
            
    #export ARFF
    def arffOutput(self):
        title = str(self.name) + '.arff'
        new_file = open(title, 'w')

        ##
        #following portions formats and writes to the new ARFF file
        ##

        #write relation
        new_file.write('@RELATION ' + str(self.name)+ '\n\n')

        #get attribute type input
        for i in range(len(self.content[0])-1):
            attribute_type = "REAL"
            new_file.write('@ATTRIBUTE ' + str("caracteristica" + str(i) + '\t' + attribute_type + '\n'))

        #create list for class attribute
        last = len(self.content[0])
        class_items = []
        for i in range(len(self.content)):
            name = self.content[i][last-1]
            if name not in class_items:
                class_items.append(self.content[i][last-1])
            else:
                pass  
        del class_items[0]
    
        string = 'class'# + '{' + ','.join(sorted(class_items)) + '}'
        new_file.write('@ATTRIBUTE class\t' +  '{'+ ','.join(sorted(class_items))+ ', ' +  str(self.content[0][last-1]) + '}' + '\n')

        #write data
        new_file.write('\n@data\n')

        del self.content[0]
        for row in self.content:
            new_file.write(','.join(row) + '\n')

        #close file
        new_file.close()
        #sleep(2)

    
#####    
run = convert()
