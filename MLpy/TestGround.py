#TODO: classify car data with a Naive Bayes classifier
#TODO: use laplace smoothing
'''
use:
       count + k
    --------------
    N + k*|classes|

'''
#TODO: Split data: 2/3 train  <-> 1/3 test (crossvalidation)
#TODO: Determine the mean error rate over 100 different random samples of training data
#TODO: plot a confusion matrix for one sample

#inspiration: http://dataaspirant.com/2017/02/06/naive-bayes-classifier-machine-learning/
#simple explaination https://www.youtube.com/watch?v=km2LoOpdB3A

'''                                       LP-Value (k=1)
class        unacc, acc, good, vgood        +4
-----------------------------------
buying       v-high, high, med, low         +4
maint        v-high, high, med, low         +4
doors        2, 3, 4, 5-more                +4
persons      2, 4, more                     +3
lug_boot     small, med, big                +3
safety       low, med, high                 +3
'''


'''
 am besten bischen googlen, wie man mit numpy spaltn, zielen und axen miteinander addidert, etc.
 auf numpy  mit "np" zugreifen
 Reihenfolge der Spalten:
 b_vh,b_h,b_m,b_l,m_vh,m_h,m_m,m_l,d_2,d_3,d_4,d_5,p_2,p_4,p_m,l_s,l_m,l_b,s_l,s_m,s_h

'''

'''
Vor count-data und nach dem Auslesen der Datei müsste noch in Training und Test-data geteilt werden, oder?
So ist ja alles Training-Data, was in der Datei steht. Darum gesplittet siehe unten
'''
'''
countmatrix = core.countData(core.ReadCardataFromCSV(cardatapath))
core.SaveMatrix2CSV(countmatrix, "count_matrix")
print(str(countmatrix))
'''

import CoreMethods as core

cardatapath = "cardaten\\car.data"

#Just testing input reading - successfull
#print(core.ReadCardataFromCSV(cardatapath).split("\n"))


data = [X for X in core.ReadCardataFromCSV(cardatapath) if len(X) > 1] #leerzeile entfernen

output = []
errors = []
for i in range(0,100):
    output = core.createRandomSample(data.copy())
    training = output[0]
    test = output[1]
    # hier erst countdata(training)
    probs = core.train(training)
    core.SaveMatrix2CSV(probs, "count_matrix_"+str(i))
    #print("matrix of probabilities")
    #print(str(probs))
    result = core.predict(test, probs)
    #correct = core.countData(test)
    errors.append(core.geterror(result, test))


errorsum = 0

for e in errors:
    errorsum += e

errorsum /= len(errors)

print('Mean Error Rate over 100 Training data is' + str(errorsum))


