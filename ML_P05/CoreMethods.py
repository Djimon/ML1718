import numpy as np
from pandas import DataFrame
import random


def SaveMatrix2CSV(matrix, filename):
    DataFrame(matrix).to_csv(filename + ".csv", sep=";", header=False)


def ReadCardataFromCSV(path):
    file = open(path, 'r')
    return str(file.read()).split("\n")

'''
class        unacc, acc, good, vgood        +4
-----------------------------------
buying       v-high, high, med, low         +4
maint        v-high, high, med, low         +4
doors        2, 3, 4, 5-more                +4
persons      2, 4, more                     +3
lug_boot     small, med, big                +3
safety       low, med, high                 +3
'''

def createRandomSample(data):
    length = len(data)
    end = length * 2 / 3
    training = []
    test = []
    for i in range(0, int(end)):
        r = random.randint(0, length-1)
        test.append(data[r])
        del data[r]
        length -= 1
    training = data
    return [training, test]


def predict(test, training, k):
    #probs ist die count-matrix
    pred_classification = []
    testdata = test

    for test in testdata:

        n_unacc = 0
        n_acc = 0
        n_good = 0
        n_vgood = 0

        for tr in training:
            difference = 0
            difference = compare(test, tr)
            # Differenz zu jedem Punkt wird berechnet
            # ToDo: muss zusammen mit jedem Trainingsdatenpunkt in Liste gespeichert werden

        #ToDo: Liste sortieren
        #ToDo: k kleinsten Differenzen wählen

        for i in range(0,k):
            print("Do something") #for-loop wants indent here, just filler
            #ToDo: für jeden dieser Datenpunkte vergleichen, welche Klasse dieser Trainingspunkt hätte
            #ToDo: diese Klasse entsprechend hochzählen (n_unacc, n_acc, n_good, n_vgood)


        # which class should be assigned (majority vote)
        highestvote = max(n_unacc, n_acc, n_good, n_vgood)

        # append class information to list
        if highestvote == n_unacc:
            pred_classification.append('unacc')
        if highestvote == n_acc:
            pred_classification.append('acc')
        if highestvote == n_good:
            pred_classification.append('good')
        if highestvote == n_vgood:
            pred_classification.append('vgood')

    return pred_classification


def compare(test, training):
    testarr = test.split(",")
    trainingarr = training.split(",")

    difference = 0

    for i in range(0, 6):
        if testarr[i] != trainingarr[i]:
            difference += 1

    return difference


'''
calculates number of false positives and false negatives in relation to 
number of predictions by comparing predicted results with correct results
'''

def geterror(result, correct):
    #correct is matrix
    #result ist list
    #print(correct)
    cor_classification = []
    count = 0
    for D in correct:
        if str(D).endswith(",unacc"):
            cor_classification.append('unacc')
        if str(D).endswith(",acc"):
            cor_classification.append('acc')
        if str(D).endswith(",good"):
            cor_classification.append('good')
        if str(D).endswith(",vgood"):
            cor_classification.append('vgood')
        count += 1


    errors = 0
    i = 0
    for R in result:
        if i < count and R != cor_classification[i]:
            errors += 1
        i += 1
    return errors/len(result), [cor_classification,result]


def CreateConfMatrix(trues, preds):
    t = 0
    p = 0
    matrix = [[0 for col in range(4)] for row in range(4)]
    for T,P in zip(trues,preds):
        print("Predicted: '{0}' <-> '{1}'".format(T,P))
        matrix[getClassIndex(T)][getClassIndex(P)] += 1
    SaveMatrix2CSV(matrix,"conf_matrix")
    print(matrix)

def getClassIndex(classlabel):
    if classlabel == "unacc":
        return 0
    if classlabel == "acc":
        return 1
    if classlabel == "good":
        return 2
    if classlabel == "vgood":
        return 3