Installation Instructions

The folder contains the three components that make up the system.
A mosquitto MQTT broker needs to be set up on the machine first. When the broker is setup the ip address and port needs to be added to the 'broker' string in each project. Install all the dependancies for the python program either through pycharm or with pip. Then the programs can be run, the datastream generator program should be run last. The timing of the data stream generators messages can be controlled with the sleep command in the code.
