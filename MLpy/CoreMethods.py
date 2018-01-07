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

def laPlace(numerator, denominator, LP_value, k=1):
    return (numerator + k) / (denominator + LP_value)


def createRandomSample(data):
    length = len(data)
    #print(data)
    #print("length = ",length)
    end = length * 2 / 3
    training = []
    test = []
    for i in range(0, int(end)):
        r = random.randint(0, length-1)
        #print("random: ",r)
        test.append(data[r])
        del data[r]
        length -= 1
    training = data
    return [training, test]

'''
    countData = Training?
    Tabelle reicht als Vorwissen, für das vorhersagen der Klasse müssen wir dynamisch aussuchen, 
    welche Tabellenwerte wir jeweils brauchen
'''
def train(training):
    return countData(training)

def predict(test, probs):
    #probs ist die count-matrix
    pred_classification = []
    testdata = test
    rows = len(testdata)
    for test in testdata:
        # get Evidence
        p_evidence = getEvidence(test, probs)

        # get probability for each possible class
        values = getprobs(test, probs, 'unacc', rows)
        p_unacc = values/ p_evidence

        values = getprobs(test, probs, 'acc', rows)
        p_acc = values/ p_evidence

        values = getprobs(test, probs, 'good', rows)
        p_good = values/ p_evidence

        values = getprobs(test, probs, 'vgood', rows)
        p_vgood = values/ p_evidence

        # which class should be assigned
        highestprobability = max(p_unacc, p_acc, p_good, p_vgood)
        #TODO: Für die Überprüfung einkommentieren
        #print("highest proba: {0} ({1},{2},{3},{4})".format(highestprobability,p_unacc,p_acc,p_good,p_vgood))

        # append class information to list
        if highestprobability == p_unacc:
            pred_classification.append('unacc')
        if highestprobability == p_acc:
            pred_classification.append('acc')
        if highestprobability == p_good:
            pred_classification.append('good')
        if highestprobability == p_vgood:
            pred_classification.append('vgood')

    return pred_classification


def ProbaEvidence(column,matrix):
    length = len(matrix)
    #print("Proba-Matrix (%d):" %length)
    #print(np.array(matrix[:,column]))
    return np.array(matrix[:,column]).sum(axis=0) / length
    #TODO: Nicht sicher ob es auch wirklich das tut, was es soll?

def getEvidence(dataline, probabilities):
    # für jede Wahrscheinlichkeit entsprechend richtige Zeile(unterste) und Spalte auswählen (Zeile=Klasse=Gesamtanzahl dieser Ausprägung der Variable; Spalte =b_vh usw.)
    arr = dataline.split(",")
    #print(arr)
    p_b = 0.000001
    p_m = 0.000001
    p_d = 0.000001
    p_p = 0.000001
    p_l = 0.000001
    p_s = 0.000001

    if arr[0] == "v-high":
        p_b = ProbaEvidence(0,probabilities)
    if arr[0] == "high":
        p_b = ProbaEvidence(1,probabilities)
    if arr[0] == "med":
        p_b = ProbaEvidence(2,probabilities)
    if arr[0] == "low":
        p_b = ProbaEvidence(3,probabilities)

    if arr[1] == "v-high":
        p_m = ProbaEvidence(4,probabilities)
    if arr[1] == "high":
        p_m = ProbaEvidence(5,probabilities)
    if arr[1] == "med":
        p_m = ProbaEvidence(6,probabilities)
    if arr[1] == "low":
        p_m = ProbaEvidence(7,probabilities)

    if arr[2] == "2":
        p_d = ProbaEvidence(8,probabilities)
    if arr[2] == "3":
        p_d = ProbaEvidence(9,probabilities)
    if arr[2] == "4":
        p_d = ProbaEvidence(10,probabilities)
    if arr[2] == "5-more":
        p_d = ProbaEvidence(11,probabilities)

    if arr[3] == "2":
        p_p = ProbaEvidence(12,probabilities)
    if arr[3] == "4":
        p_p = ProbaEvidence(13,probabilities)
    if arr[3] == "more":
        p_p = ProbaEvidence(14,probabilities)

    if arr[4] == "small":
        p_l = ProbaEvidence(15,probabilities)
    if arr[4] == "med":
        p_l = ProbaEvidence(16,probabilities)
    if arr[4] == "big":
        p_l = ProbaEvidence(17,probabilities)

    if arr[5] == "low":
        p_s = ProbaEvidence(18,probabilities)
    if arr[5] == "med":
        p_s = ProbaEvidence(19,probabilities)
    if arr[5] == "high":
        p_s = ProbaEvidence(20,probabilities)

    res = p_b*p_m*p_d*p_p*p_l*p_s
    return res

def getprobs(dataline, probabilities, testedclass,rowcount):
    # probabilities = count-matrix
    tableline = 100 # falls, irgendwas falsch läuft, wir dadurch ein index out of bounce error provoziert.
    p_b = 0.000001
    p_m = 0.000001
    p_d = 0.000001
    p_p = 0.000001
    p_l = 0.000001
    p_s = 0.000001

    # tableline gibt an, welche Zeile/Klasse der Tabelle gemeint ist
    if testedclass == 'unacc':
        tableline = 0
    if testedclass == 'acc':
        tableline = 1
    if testedclass == 'good':
        tableline = 2
    if testedclass == 'vgood':
        tableline = 3

    #print("Tableline = ",tableline," class = ",testedclass)

    #b_vh, b_h, b_m, b_l, m_vh, m_h, m_m, m_l, d_2, d_3, d_4, d_5, p_2, p_4, p_m, l_s, l_m, l_b, s_l, s_m, s_h, c_unacc, c_acc, c_good, c_vgood
    # für jede Wahrscheinlichkeit entsprechend richtige Zeile(tableline) und Spalte auswählen (Zeile=Klasse=testedClass; Spalte =b_vh usw.)
    arr = dataline.split(",")
    if arr[0] == "v-high":
        p_b = probabilities[tableline][0] / rowcount
    if arr[0] == "high":
        p_b = probabilities[tableline][1] / rowcount
    if arr[0] == "med":
        p_b = probabilities[tableline][2] / rowcount
    if arr[0] == "low":
        p_b = probabilities[tableline][3] / rowcount

    if arr[1] == "v-high":
        p_m = probabilities[tableline][4] / rowcount
    if arr[1] == "high":
        p_m = probabilities[tableline][5] / rowcount
    if arr[1] == "med":
        p_m = probabilities[tableline][6] / rowcount
    if arr[1] == "low":
        p_m = probabilities[tableline][7] / rowcount

    if arr[2] == "2":
        p_d = probabilities[tableline][8] / rowcount
    if arr[2] == "3":
        p_d = probabilities[tableline][9] / rowcount
    if arr[2] == "4":
        p_d = probabilities[tableline][10] / rowcount
    if arr[2] == "5-more":
        p_d = probabilities[tableline][11] / rowcount

    if arr[3] == "2":
        p_p = probabilities[tableline][12] / rowcount
    if arr[3] == "4":
        p_p = probabilities[tableline][13] / rowcount
    if arr[3] == "more":
        p_p = probabilities[tableline][14] / rowcount

    if arr[4] == "small":
        p_l = probabilities[tableline][15] / rowcount
    if arr[4] == "med":
        p_l = probabilities[tableline][16] / rowcount
    if arr[4] == "big":
        p_l = probabilities[tableline][17] / rowcount

    if arr[5] == "low":
        p_s = probabilities[tableline][18]/ rowcount
    if arr[5] == "med":
        p_s = probabilities[tableline][19] / rowcount
    if arr[5] == "high":
        p_s = probabilities[tableline][20] / rowcount

    p_c = probabilities[tableline][21+tableline]/ rowcount

    res = p_b * p_m * p_d * p_p * p_l * p_s * p_c

    return res



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
        if R != cor_classification[i]:
            errors += 1
        i += 1
    return errors/len(result), [cor_classification,result]


def CreateConfMatrix(trues, preds):
    t = 0
    p = 0
    matrix = [[0 for col in range(4)] for row in range(4)]
    for T in trues:
        for P in preds:
            print("Predicted: '{0}' <-> '{1}'".format(T,P))
            matrix[getClassIndex(T)][getClassIndex(P)] += 1
    SaveMatrix2CSV(matrix,"conf_matrix")

def getClassIndex(classlabel):
    if classlabel == "unacc":
        return 0
    if classlabel == "acc":
        return 1
    if classlabel == "good":
        return 2
    if classlabel == "vgood":
        return 3

def countData(data):
    # print("Count input data:")
    # print(str(data))
    # print("========================")
    unacc = []
    acc = []
    good = []
    vgood = []
    #datalist = data.split("\n")
    # print(str(datalist))
    # print("========================")
    for D in data:
        if str(D).endswith(",unacc"):
            unacc.append(CountClassProperties(D))
        if str(D).endswith(",acc"):
            acc.append(CountClassProperties(D))
        if str(D).endswith(",good"):
            good.append(CountClassProperties(D))
        if str(D).endswith(",vgood"):
            vgood.append(CountClassProperties(D))

    unacc_res = np.array(unacc).sum(axis=0)
    acc_res = np.array(acc).sum(axis=0)
    good_res = np.array(good).sum(axis=0)
    vgood_res = np.array(vgood).sum(axis=0)
    #print("Traiing:")
    #print(unacc_res,acc_res,good_res,vgood_res)
    return np.array([unacc_res, acc_res, good_res, vgood_res])


def CountClassProperties(datalist):
    #print("Counting element: " + str(datalist))
    # buying
    b_vh = 0  # buing, v-high
    b_h = 0  # buing, high
    b_m = 0  # buing, med
    b_l = 0  # buing, low
    # maint
    m_vh = 0
    m_h = 0
    m_m = 0
    m_l = 0
    # doors
    d_2 = 0
    d_3 = 0
    d_4 = 0
    d_5 = 0
    # persons
    p_2 = 0
    p_4 = 0
    p_m = 0
    # lug_boot
    l_s = 0
    l_m = 0
    l_b = 0
    # safety
    s_l = 0
    s_m = 0
    s_h = 0
    #class
    c_unacc = 0
    c_acc = 0
    c_good = 0
    c_vgood = 0

    arr = datalist.split(",")
    if arr[0] == "v-high":
        b_vh += 1
    if arr[0] == "high":
        b_h += 1
    if arr[0] == "med":
        b_m += 1
    if arr[0] == "low":
        b_l += 1

    if arr[1] == "v-high":
        m_vh += 1
    if arr[1] == "high":
        m_h += 1
    if arr[1] == "med":
        m_m += 1
    if arr[1] == "low":
        m_l += 1

    if arr[2] == "2":
        d_2 += 1
    if arr[2] == "3":
        d_3 += 1
    if arr[2] == "4":
        d_4 += 1
    if arr[2] == "5-more":
        d_5 += 1

    if arr[3] == "2":
        p_2 += 1
    if arr[3] == "4":
        p_4 += 1
    if arr[3] == "more":
        p_m += 1

    if arr[4] == "small":
        l_s += 1
    if arr[4] == "med":
        l_m += 1
    if arr[4] == "big":
        l_b += 1

    if arr[5] == "low":
        s_l += 1
    if arr[5] == "med":
        s_m += 1
    if arr[5] == "high":
        s_h += 1

    if arr[6] == "unacc":
        c_unacc += 1
    if arr[6] == "acc":
        c_acc += 1
    if arr[6] == "good":
        c_good += 1
    if arr[6] == "vgood":
        c_vgood += 1

    return [b_vh, b_h, b_m, b_l, m_vh, m_h, m_m, m_l, d_2, d_3, d_4, d_5, p_2, p_4, p_m, l_s, l_m, l_b, s_l, s_m, s_h,c_unacc,c_acc,c_good,c_vgood]
