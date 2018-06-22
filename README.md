# vertx-eventbus-tcp-bridge-mystery

Showcase of improperly working event-bus over TCP bridge between two
independent vertices.

## Goal

The goal is to have two independent verticles developed in two separate
and independent modules (projects) who are able to communicate with each
other using event-bus over TCP bridge.

## Environment

Both verticles developed in two modules “NodeA” and “NodeB” using Kotlin
language and Gradle build system.

Verticle from “NodeA” is running on the virtual machine (hosted locally
using Virtual Box) and having IP **192.168.56.101** while vertical from
“NodeB” running on localhost having IP **192.168.56.1**

For the sake of demonstrations, both verticles are same behavior.
They set up TCP bridge, registering for events on their own address and
sending every second message for their peer using two different methods:
* Natively over event-bus
* Using frame helper over TCP socket

## Problem

Only messages sent via TCP socket using frame helper are arriving at the
other ends but messages sent from the native event-bus are not.
There are no errors nor exceptions so it is not clear what is the
problem with message delivery using native event-bus over TCP bridge.
