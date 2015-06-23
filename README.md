# asterisk-scala

The opensource library written in Scala to integrate your great apps with Asterisk PBX.

The library provides capabilities to interact with Asterisk through AGI & AMI.

This library implements reactive, asynchronous, non-blocking approach in handling the requests.

## Current status: 

**AGI**: under development

**AMI**: not started


# AGI

*TODO:* add description of `simpleagiserver`

    import com.github.bespalovdn.asteriskscala.agi.command.{Hangup, Playback}
    import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
    import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest

    import scala.concurrent.Future
    
    class AgiHandler extends AgiRequestHandler
    {
        override def handle(request: AgiRequest): Future[Unit] = {
            Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture
        }
    }