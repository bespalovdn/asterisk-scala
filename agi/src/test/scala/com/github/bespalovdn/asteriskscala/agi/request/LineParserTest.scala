package com.github.bespalovdn.asteriskscala.agi.request

import com.github.bespalovdn.asteriskscala.common.test.TestSupport
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LineParserTest extends TestSupport
{
    "LineParserTest" should
    "check correctness parsing the AGI variables" in {
        assert(parse("agi_request: HELLO\n") == ("request", "HELLO"))
        assert(parse("agi_request: HELLO, world!\n") == ("request", "HELLO, world!"))
        //missing end of line:
        assert(parse("agi_request: HELLO") == null)
    }

    private def parse(line: String): (String, String) = AgiRequest.lineParser.parse(line)
}
