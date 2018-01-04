import numpy as np
from pandas import DataFrame
import random


def SaveMatrix2CSV(matrix, filename):
    DataFrame(matrix).to_csv(filename + ".csv", sep=";", header=False)


def ReadCardataFromCSV(path):
    file = open(path, 'r')
    return str(file.read())


def laPlace(numerator, denominator, LP_value, k=1):
    return (numerator + k) / (denominator + LP_value)


def createRandomSample(data):
    end = len(data) * 2 / 3
    samples = [2]
    for i in range(0, end):
        r = random.randint[0, len(data)]
        samples[0] = samples.append(samples, data[r])
        #TODO: Eintrag entfernen
        # data.pop[r]
    samples[1] = data
    return samples

'''
    countData = Training?
    Tabelle reicht als Vorwissen, für das vorhersagen der Klasse müssen wir dynamisch aussuchen, 
    welche Tabellenwerte wir jeweils brauchen
'''
def train(training):
    return countData(training)

def predict(test, probs):
    pred_classification = []
    #...
    # pred_classification wird für jeden Eintrag der Test-Daten ein String in die Liste angehängt wie bei geterror
    return pred_classification

'''
calculates number of false positives and false negatives in relation to 
number of predictions by comparing predicted results with correct results
'''

def geterror(result, correct):
    data = correct.split("\n")
    cor_classification = []
    for D in correct:
        if str(D).endswith(",unacc"):
            cor_classification.append(cor_classification, 'unacc')
        if str(D).endswith(",acc"):
            cor_classification.append(cor_classification, 'acc')
        if str(D).endswith(",good"):
            cor_classification.append(cor_classification, 'good')
        if str(D).endswith(",vgood"):
            cor_classification.append(cor_classification, 'vgood')

    errors = 0
    for i in result:
        if result[i] != cor_classification[i]:
            errors += 1
    res = errors/len(result)
    return res



def countData(data):
    # print("Count input data:")
    # print(str(data))
    # print("========================")
    unacc = []
    acc = []
    good = []
    vgood = []
    datalist = data.split("\n")
    # print(str(datalist))
    # print("========================")
    for D in datalist:
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

    return np.array([unacc_res, acc_res, good_res, vgood_res])


def CountClassProperties(datalist):
    print("Counting element: " + str(datalist))
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

    return [b_vh, b_h, b_m, b_l, m_vh, m_h, m_m, m_l, d_2, d_3, d_4, d_5, p_2, p_4, p_m, l_s, l_m, l_b, s_l, s_m, s_h]
