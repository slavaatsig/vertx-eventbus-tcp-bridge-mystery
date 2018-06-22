# NodeA

A verticle that supposed to be run on a remote machine.

This project has a gradle SSH plugin for deploying verticle’s fat jar
onto a virtual machine.

In `gradle.build` it is assumed that machine is accessible on IP:
**192.168.56.101** with username and password being: **“ubuntu”**, you
can adjust it for your setup (lines 95—97.)

To deploy it a virtual machine using gradle wrapper from the shell:
`./gradlew deploy_to_virtual_box`