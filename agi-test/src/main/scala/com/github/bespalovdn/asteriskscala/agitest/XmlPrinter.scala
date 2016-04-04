package com.github.bespalovdn.asteriskscala.agitest

import scala.xml.{Elem, Node, PrettyPrinter, Utility}

trait XmlPrinter
{
    def xmlHeader = """<?xml version="1.0" encoding="utf-8"?>"""
    def print(node: Node): String
    def printDocument(root: Elem): String
}

trait XmlTrimPrinter extends XmlPrinter
{
    override def print(node: Node): String = Utility.trim(node).toString()
    override def printDocument(root: Elem): String = xmlHeader + print(root)
}

object XmlTrimPrinter extends XmlTrimPrinter

trait XmlPrettyPrinter extends XmlPrinter
{
    def lineWidth = 150
    def tabStep = 2

    override def print(node: Node): String = new PrettyPrinter(lineWidth, tabStep).format(node)
    override def printDocument(root: Elem): String = xmlHeader + "\n" + print(root)
}

object XmlPrettyPrinter extends XmlPrettyPrinter