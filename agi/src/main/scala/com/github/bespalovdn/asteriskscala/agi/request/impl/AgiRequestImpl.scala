package com.github.bespalovdn.asteriskscala.agi.request.impl

import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest

private [request]
class AgiRequestImpl(source: Map[String, String]) extends AgiRequest
{
    override lazy val direction: String = source("network_script")

    override lazy val arguments: Seq[String] = argumentsKV.values.toSeq

    override lazy val argumentsKV: Map[String, String] = source.filter{case (k, _) => k.startsWith("arg_")}

    override lazy val url: String = source.get("request").orNull

    override lazy val channel: String = source.get("channel").orNull

    override lazy val uniqueId: String = source.get("uniqueid").orNull

    override lazy val channelType: String = source.get("type").orNull

    override lazy val language: String = source.get("language").orNull

    override lazy val callerIdNum: String = source.get("callerid").orNull

    override lazy val callerIdName: String = source.get("calleridname").orNull

    override lazy val dnid: String = source.get("dnid").orNull

    override lazy val rdnis: String = source.get("rdnis").orNull

    override lazy val dialplanContext: String = source.get("context").orNull

    override lazy val dialplanExtension: String = source.get("extension").orNull

    override lazy val dialplanPriority: Int = source.getOrElse("priority", "0").toInt

    override lazy val accountCode: String = source.get("accountcode").orNull

    override lazy val asteriskVersion: String = source.get("version").orNull

    override lazy val callingpres: String = source.get("callingpres").orNull

    override lazy val callingani2: String = source.get("callingani2").orNull

    override lazy val callington: String = source.get("callington").orNull

    override lazy val callingtns: String = source.get("callingtns").orNull

    override lazy val isEnhancedAgi: Boolean = source.get("enhanced") match {
        case Some("1.0") => true
        case _ => false
    }
}
