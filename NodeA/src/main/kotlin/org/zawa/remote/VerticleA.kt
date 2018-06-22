package org.zawa.remote

import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.core.net.NetServerOptions
import io.vertx.core.net.NetSocket
import io.vertx.ext.bridge.BridgeOptions
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge
import io.vertx.ext.eventbus.bridge.tcp.impl.protocol.FrameHelper

class VerticleA : io.vertx.core.AbstractVerticle() {
    val myAddr = "A"
    val myIP = "192.168.56.101"

    val otherAddr = "B"
    val otherIP = "192.168.56.1"

    override fun start() {
        val vertx = Vertx.vertx()
        val netOpts = NetServerOptions().setPort(7000).setHost(myIP)
        val bridgeOpts = BridgeOptions().apply {
            addInboundPermitted(PermittedOptions().setAddressRegex(".*"))
            addOutboundPermitted(PermittedOptions().setAddressRegex(".*"))
        }

        TcpEventBusBridge.create(vertx, bridgeOpts, netOpts).listen(netOpts.port, netOpts.host) {
            if (it.succeeded()) {
                println("Node $myAddr: Bridge is up.")

                // Set consumer for messages addressed to me
                vertx.eventBus().consumer<Any>(myAddr) { msg: Message<Any> ->
                    println("Node $myAddr: Received message: ${msg.body()}")
                }

                val usingEventBus = JsonObject().put("sent-from", myAddr).put("method", "event-bus")
                val usingSocket = JsonObject().put("sent-from", myAddr).put("method", "frame-helper")

                // Publish every second to other verticle, using two different payloads for different methods
                vertx.setPeriodic(1000) {
                    println("Node $myAddr: Sending message to $otherAddr using event bus")
                    vertx.eventBus().send(otherAddr, usingEventBus)

                    println("Node $myAddr: Sending message to $otherAddr using TCP socket")
                    vertx.createNetClient().connect(7000, otherIP) { ar: AsyncResult<NetSocket> ->
                        FrameHelper.sendFrame("publish", otherAddr, usingSocket, ar.result())
                    }
                }
            } else {
                println("Node $myAddr: Bridge is down: ${it.cause()}")
            }
        }
    }
}