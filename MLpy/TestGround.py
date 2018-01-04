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

import CoreMethods as core

cardatapath = "cardaten\\car.data"

#Just testing input reading - successfull
#print(core.ReadCardataFromCSV(cardatapath).split("\n"))

#TODO: Anhand der Count-matrix (ein Numpy-Array) können nun theoretisch alle Wahrscheinlichkeiten berehcnet werden
'''
 am besten bischen googlen, wie man mit numpy spaltn, zielen und axen miteinander addidert, etc.
 auf numpy  mit "np" zugreifen
 
'''
countmatrix = core.countData(core.ReadCardataFromCSV(cardatapath))
core.SaveMatrix2CSV(countmatrix, "count_matrix")
print(str(countmatrix))
