package com.github.bespalovdn.asteriskscala.common.akka

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}

import scala.collection.mutable
import scala.reflect.ClassTag

trait ActorFactory
{
    implicit class Factory(factory: ActorRefFactory){
        def newActor[A <: Actor : ClassTag](name: String)(constructor: => A): ActorRef =
            factory.actorOf(Props(constructor), UniqueNamesTable.uniqueNameFor(factory, name))
        def newActor(name: String, props: Props): ActorRef =
            factory.actorOf(props, UniqueNamesTable.uniqueNameFor(factory, name))
    }

    def apply(factory: ActorRefFactory): Factory = new Factory(factory)
}

private object UniqueNamesTable
{
    def uniqueNameFor(factory: ActorRefFactory, name: String): String = {
        val unames = namesTable.synchronized{namesTable.getOrElseUpdate(factory, mutable.HashMap.empty)}
        val id = unames.synchronized{
            val id = unames.getOrElse(name, 0) + 1
            unames += name -> id
            id
        }
        name + "-" + id
    }

    private val namesTable = mutable.WeakHashMap.empty[ActorRefFactory, mutable.HashMap[String, Int]]
}
