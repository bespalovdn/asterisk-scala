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

In this example we create the chain of AGI commands:

    Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture

All the commands in this chain executed consequentially. 
The second command in chain will be executed only after first command successfully finished, and so on.
 
All the commands in this chain executed in non-blocking fashion. It means, the thread sending the AGI request will not
stop in waiting state, until AGI response received. While request is being processed by the Asterisk, freed thread 
can serve next AGI request. This feature allows us to affirm: this library designed for high-loaded services, 
able to process thousands AGI requests simultaneously.  

On the transport level we're using asynchronous networking library `Netty`, which uses Java's NIO sockets in turn.

Back to the chain of commands in our example.
Consider `Playback("demo-congrats")` command. This is factory method that returns instance of `AgiCommand` class.
When method `send()` is being called on `AgiCommand`, then real action is being executed. AGI command is going to the Asterisk. 
The result of `AgiCommand.send()` is `Future[SuccessResponse]`. 
If Asterisk successfully executes the command, the future completes with `SuccessResponse`.
If something goes wrong, the future completes with `scala.util.Failure`, which contains the reason of fail.
You free to use any functions on `scala.concurrent.Future` when dealing with AGI commands. 


Operator `>>` is something like glue, that puts two commands together in sequence. This operator defined in following trait:

    implicit class FutureOps[A](f: Future[A])(implicit context: ExecutionContext)
    {
        def >>= [B](handler: A => Future[B]): Future[B] = f flatMap handler
        def >> [B](handler: => Future[B]): Future[B] = f flatMap {_ => handler}
    }
    
