# asterisk-scala

The opensource library written in Scala to integrate your great apps with Asterisk PBX.

The library provides capabilities to interact with Asterisk through AGI & AMI.

This library implements reactive, asynchronous, non-blocking approach in handling the requests.

## Current status: 

**AGI**: under development

**AMI**: not started


# AGI

## Example: `simpleagiserver`

This example will demonstrate minimal code you have to write in order to create simple AGI server.

In this simple example our AGI server will execute `Playback` command to play standard "demo-congrats" phrase, 
and then `Hangup` command. 

At first, you have to create instance of `AgiRequestHandler`:

    class AgiHandler extends AgiRequestHandler
    {
        override def handle(request: AgiRequest): Future[Unit] = {
            Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture
        }
    }
    
This handler will process single AGI request. I.e. for every single AGI request the `AgiServer` will create their own `AgiHandler` instance.

The logic of handler should be placed into `handle(request: AgiRequest): Future[Unit]` method.

Since the library announced as fully asynchronous, the result of `handle` method is `Future` of `Unit` type. 
You have to keep in mind the primary rule: your code inside this `handle` method should be implemented in non-blocking manner.
If you has to block by some reason (e.g. read some file from the disk, or get data from DB), you have to 
wrap blocking code into separated execution context. Consider `scala.concurrent.blocking` for example. 