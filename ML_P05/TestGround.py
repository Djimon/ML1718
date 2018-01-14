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
import time

'''

 Reihenfolge der Spalten:
 b_vh,b_h,b_m,b_l,m_vh,m_h,m_m,m_l,d_2,d_3,d_4,d_5,p_2,p_4,p_m,l_s,l_m,l_b,s_l,s_m,s_h

'''

'''
++++ ToDo: k at runtime
ToDo: compute and evaluate for k=1 und k=5
ToDo: combine distance with training point, sort this combined list
ToDo: compare with classifications from previous assignments, especially Naive Bayes
'''

import CoreMethods as core
import sys

cardatapath = "cardaten\\car.data"

data = [X for X in core.ReadCardataFromCSV(cardatapath) if len(X) > 1] #leerzeile entfernen

k = input("How many nearest neighbors should be considered for classifying?")

if not k.isnumeric():
    print("No valid input. Exiting program")
    sys.exit()


output = []
errors = []
max = 1
print("running kNN %d times to calculate error" %max)
for i in range(0,max):
    t0 = time.time()
    training, test = core.createRandomSample(data.copy())
    result = core.predict(test, training, int(k))
    error, conf = core.geterror(result, test)
    errors.append(error)
    core.CreateConfMatrix(conf[0], conf[1])
    print("result: e = " + str(error) + ", time: " + str(time.time() - t0) + " - " + str(result))
    #print(i," from ",max)


errorsum = 0

for e in errors:
    errorsum += e


#print("Mean Error Rate over " +str(max)+ " Training data is " + str(errorsum))


