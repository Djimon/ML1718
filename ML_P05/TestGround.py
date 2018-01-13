'''
     Table
                                     LP-Value (k=1)
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

 Reihenfolge der Spalten:
 b_vh,b_h,b_m,b_l,m_vh,m_h,m_m,m_l,d_2,d_3,d_4,d_5,p_2,p_4,p_m,l_s,l_m,l_b,s_l,s_m,s_h

'''

'''
++++ ToDo: k at runtime
ToDo: compute and evaluate for k=1 und k=5
ToDo: combine distance with training point, sort this combined list
++++ ToDo: majority vote
++++ ToDo: learn data (store)
++++ ToDo: distance (dissimilarity measure) --> Hamming Distance
++++ ToDo: chose two third as training, one third as test data randomly
++++ ToDo: 100 samples 
++++ ToDo: determine mean error rate
++++ ToDo: plot confusion matrix
ToDo: compare with classifications from previous assignments, especially Naive Bayes
'''

import CoreMethods as core
import sys

cardatapath = "cardaten\\car.data"

data = [X for X in core.ReadCardataFromCSV(cardatapath) if len(X) > 1] #leerzeile entfernen

k = input("How many nearest neighbors should be considered for classifying?")

if k.isnumeric() == false:
    print("No valid input. Exiting program")
    sys.exit()


output = []
errors = []
for i in range(0,100):
    output = core.createRandomSample(data.copy())
    training = output[0]
    test = output[1]
    result = core.predict(test, training, k)
    error, conf = core.geterror(result, test)
    errors.append(error)
    if i == 50:
        core.CreateConfMatrix(conf[0], conf[1])


errorsum = 0

for e in errors:
    errorsum += e

errorsum /= len(errors)

print('Mean Error Rate over 100 Training data is ' + str(errorsum))


