package com.github.bespalovdn.asteriskscala.agi.request

import org.scalatest._

class LineParserTest extends FlatSpecLike
    with Assertions
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
{
    "LineParserTest" should
    "check correctness parsing the AGI variables" in {
        //valid examples:
        assert(parse("agi_request: HELLO\n") == ("request", "HELLO"))
        assert(parse("agi_request: HELLO, world!\n") == ("request", "HELLO, world!"))
        //invalid: missing end of line:
        assert(parse("agi_request: HELLO") == null)
    }

    private def parse(line: String): (String, String) = AgiRequest.lineParser.parse(line)
}
