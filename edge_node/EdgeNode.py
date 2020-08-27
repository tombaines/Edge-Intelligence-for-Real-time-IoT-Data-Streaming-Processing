import string

import paho.mqtt.client as mqtt
import numpy as np
from sklearn.cluster import DBSCAN
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt


#######################################################################
# MQTT SETUP
#######################################################################

def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    client.subscribe("data")


def on_message(client, userdata, msg):
    incomingmsg = msg.payload.decode("utf-8")
    dataSplit = incomingmsg.split(",")
    dataSplit = [float(i) for i in dataSplit]
    ds.append(dataSplit)


def on_publish(client, userdata, result):
    print("data published \n")
    pass

#insert broker ip address in the broker string.
broker = ""
port = 1883
ds = []
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message
client.on_publish = on_publish
client.connect(broker, port, 60)

########################################################################
# DBSCAN
########################################################################
averageClusters = 0
passes = 0
totalNoise = 0
while len(ds) < 10000:
    if len(ds) == 1000:
        print(ds)
        X = ds
        X = StandardScaler().fit_transform(X)

        db = DBSCAN(eps=0.045, min_samples=2).fit(X)
        core_samples_mask = np.zeros_like(db.labels_, dtype=bool)
        core_samples_mask[db.core_sample_indices_] = True
        labels = db.labels_
        n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)
        n_noise_ = list(labels).count(-1)

        print('Estimated number of clusters: %d' % n_clusters_)
        print('Estimated number of noise points: %d' % n_noise_)

        #######################################################################
        # Visualise Data
        #######################################################################
        unique_labels = set(labels)
        colors = [plt.cm.Spectral(each)
                  for each in np.linspace(0, 1, len(unique_labels))]
        for k, col in zip(unique_labels, colors):

            class_member_mask = (labels == k)

            xy = X[class_member_mask & core_samples_mask]
            plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=tuple(col),
                     markersize=1)

            xy = X[class_member_mask & ~core_samples_mask]
            plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=tuple(col),
                     markersize=2)

        plt.title('Estimated number of clusters: %d' % n_clusters_ + ' - Estimated Noise: ' + str(n_noise_))
        plt.show()

        dataWithGroups = []
        testDatalist = []

        #######################################################################
        # Transform Data
        #######################################################################

        for f, b in zip(ds, labels):
            testlist2 = f[0], f[1], b
            dataWithGroups.append(testlist2)
        amountOfClusters = max(labels)
        amountOfClusters = amountOfClusters + 1
        print(dataWithGroups)

        for z in range(amountOfClusters):
            seriesInCluster = []
            entryCounter = 0
            tempValue = 0

            for i in dataWithGroups:
                if i[2] == z:
                    tempValue = tempValue + i[1]
                    entryCounter = entryCounter + 1
                    seriesInCluster.append(i[0])
            averageTemp = round(tempValue / entryCounter, 3)
            seriesInCluster.insert(0, averageTemp)
            testDatalist.append(seriesInCluster)
        print(testDatalist)
        message = ""
        for x in testDatalist:
            message = message + str(x) + "/"
            message = message.replace(" ", "")
        ret = client.publish("clus", message)

        ################################################################c#######
        # Calculating Metrics
        #######################################################################

        totalNoise = totalNoise + n_noise_
        passes = passes +1
        averageClusters = averageClusters + n_clusters_
        if passes == 100:
            averageClusters = averageClusters/passes
            print("averageClusters:")
            print(averageClusters)
            print("total noise:")
            print(totalNoise)

        ds.clear()

    client.loop()
