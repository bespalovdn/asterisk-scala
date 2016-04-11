# asterisk-scala

The opensource library written in Scala to integrate your great apps with Asterisk PBX.

This library provides capabilities to interact with Asterisk through AGI & AMI.

This library implements reactive (asynchronous, non-blocking) approach in handling the requests.
It designed to handle thousands concurrent AGI/AMI requests simultaneously.

## Milestones: 

* AGI + UT (in progress)
* AMI + UT (not started)
* Documentation (in progress)
* Uploading to central storage (not started)

# AGI

## Example: `simpleagiserver`

This example will demonstrate the minimal code you have to write in order to create simple AGI server.
In this example our AGI server will execute `Playback` command to play standard "demo-congrats" phrase, 
and then `Hangup` command. 

At first, you have to create the instance of `AgiRequestHandler`. 
This class will contain main business-logic of AGI request handler: 

    class AgiHandler extends AgiRequestHandler
    {
        override def handle(request: AgiRequest): Future[Unit] = {
            Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture
        }
    }
    
This handler will process single AGI request. 
I.e. for every single AGI request the `AgiServer` will create their own `AgiHandler` instance.
We will back to details of this class implementation a bit later.

The second class you have to create, is `AgiHandlerFactory`:

    class AgiHandlerFactory extends AgiRequestHandlerFactory
    {
        override def createHandler(): AgiRequestHandler = new AgiHandler()
    }
    
The `AgiServer` instance will use this factory to create an instance of `AgiHandler`.
Each time the new `AGI` request received by `AgiServer`, 
the `AgiHandlerFactory.createHandler()` method is going to call by `AgiServer`.

Finally, you have to create an instance of `AgiServer`:

    object Main
    {
        val agiBindAddr = InetSocketAddress.createUnresolved("0.0.0.0", 5000)
    
        def main(args: Array[String]): Unit ={
            // create the AgiServer instance
            val server = new AgiServer(agiBindAddr, new AgiHandlerFactory)
            // run the server
            val lifetime = server.run()
            // wait until server stopped
            Await.ready(lifetime.stopped, Duration.Inf)
        }
    }

The comments in code gives detailed presentation about what's going on here.
    
Now, back to the `AgiHandler's` implementation:

    class AgiHandler extends AgiRequestHandler
    {
        override def handle(request: AgiRequest): Future[Unit] = {
            Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture
        }
    }

The logic of handler should be placed into `def handle(request: AgiRequest): Future[Unit]` method.

Since the library announced as fully asynchronous, the result of `handle` method is `Future` of `Unit` type. 
You have to keep in mind the primary rule: your code inside this `handle` method should be implemented in non-blocking manner.
If you has to block by some reason (e.g. read some file from the disk, or get data from DB), you have to 
wrap blocking code into separated execution context. Consider `scala.concurrent.blocking` for example. 

In this example we create the chain of AGI commands:

    Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture

Every command in chain has following signature:

    trait AgiCommand
    {
        def send()(implicit sender: AgiCommandSender): Future[SuccessResponse]
    }
    
Executing `send()` method on the `AgiCommand`, the real sending of `AGI` request is going to perform.
If Asterisk successfully executes the command, the future completes with `SuccessResponse`.
If something goes wrong, the future completes with `scala.util.Failure`, which contains the reason of fail.
You free to use any functions on `scala.concurrent.Future` when dealing with AGI commands. 

Now look at the commands in the chain: `a >> b >> c`.
All the commands in the chain executed consequentially. 
The second command in chain will be executed only after the first one has successfully finished, and so on.
This rule is being worked due to `>>` operator.

Operator `>>` is something like glue, that puts two commands together in sequence. 
This operator defined in following trait:

    implicit class FutureOps[A](f: Future[A])(implicit context: ExecutionContext)
    {
        def >>= [B](handler: A => Future[B]): Future[B] = f flatMap handler
        def >> [B](handler: => Future[B]): Future[B] = f flatMap {_ => handler}
    }
    
that is:

* This operator is generic on its parameters, i.e. you may use it on any type of Future.
* This operator declares: right-side command will be executed only AFTER left-side command complete.
  This simple rule allows you to build chains of commands that should be executed one-by-one.
  
There is also useful `>>=` operator in FutureOps trait. 
This operator allows to use lambda in cases, when result of first command is being used in second command as input parameter.
For example, you able to use scala's `for` expression to deal with futures and its results:

    for(
        GetVariableResponse.Success(a) <- GetVariable("EXTEN").send();
        b <- SetVariable("TMP", a).send()
    ) yield b
    
But, in such simple cases, it could be easier to type:

    GetVariable("EXTEN").send() >>= {case GetVariableResponse.Success(a) => SetVariable("TMP", a).send()}

`FutureOps` trait declared in trait `FutureExtensions`. 
So you may use this functionality outside of `AgiRequestHandler` as well.

Last noteworthy thing about this example is necessity to use `().toFuture` at the end of chain of commands.
According to declaration, result of `handle` method is `Future[Unit]`, 
but result of `Hangup.send()` command is `Future[SuccessResponse]` type. 
So this last command `().toFuture` is kind of transformation the result of penultimate command to the `Future[Unit]`.
Note, that `toFuture` helper method is defined in trait:

    implicit class FutureBuilder[A](value: A)
    {
        def toFuture: Future[A] = Promise.successful(value).future
    }
    
which is placed in same `FutureExtensions` trait. Thus you may use it outside of `AgiRequestHandler`.

## Some common states about this library.

This library designed with strong concurrency principles at the core.  
Each sending of `AgiCommand` is being executed in non-blocking fashion. 
It means, the thread sending the `AGI` request will not stop, waiting until the `AGI` response received. 
While request is being processed by the Asterisk, working thread can serve next `AGI` request. 
This feature allows to handle a lot of `AGI` requests/commands simultaneously.  

On the transport level we're using the `Netty` library, which uses Java's NIO sockets in turn.


# Testing the AGI server.

TODO: add samples of testing.
