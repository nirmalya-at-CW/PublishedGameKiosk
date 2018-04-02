package com.OneHuddle.Quiz.preparation.parsing

import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, FailedToParseAnswersAvailableInDB, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, PossibleAnswer}
import org.scalatest.{BeforeAndAfterAll, FunSuite, MustMatchers, WordSpecLike}
import org.scalatest.Inspectors._
import org.json4s.{DefaultFormats, Formats, JValue, ShortTypeHints}
import org.json4s.jackson.Serialization._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.JsonDSL._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport

/**
  * Created by nirmalya on 12/3/18.
  */
class AnswerOptionParsingTestSuite extends FunSuite
  with MustMatchers
  with BeforeAndAfterAll {

  implicit val f = org.json4s.DefaultFormats
  implicit val formats_2 = Serialization.formats(
    ShortTypeHints(
      List(
        classOf[AnswersAvailableInDB]
      )
    )
  )


  test("AnswerOptions in a JSON string with only objective answer - as available in DB - is parsed as expected") {


    val sampleAnswerOption = "{\"subjective\":null,\"objective\":{\"options\":[{\"option1\":\"N\"},{\"option2\":\"N\"},{\"option3\":\"Y\"},{\"option4\":\"N\"}]},\"total_options\":4,\"correct_options\":2}}"
    

    /*val j = List(("option1","N"),("option2","N"),("option3","Y"),("option4","N"))

    val objective = Options(j)

    val k =  AnswersAvailableInDB(None,objective,4,2)

    println(write(k))*/


    val b = parse(sampleAnswerOption).extract[AnswersAvailableInDB]
    val parsedAnswerOptions = AnswerBuilder.assembleAvailableAnswers(b)

    assert(parsedAnswerOptions._1.isEmpty == true && parsedAnswerOptions._2.isEmpty ==  false)

    forAll(parsedAnswerOptions._2.get) {
      ans => assert(ans.isInstanceOf[PossibleAnswer] &&
                    (ans.isInstanceOf[ObjectiveIncorrectAnswer] || ans.isInstanceOf[ObjectiveCorrectAnswer]))
    }

    assert(parsedAnswerOptions._2.get.toIndexedSeq(1).humanReadableAnswerText === "option2")

  }

  test("AnswerOptions in a JSON string with only subjective answer - as available in DB - is parsed as expected") {


    val sampleAnswerOption = "{\"subjective\":\"subjective answer\",\"objective\":null,\"total_options\":4,\"correct_options\":2}}"


    val b = AnswerBuilder.unJasonifyAnswers(sampleAnswerOption)
    val parsedAnswerOptions = AnswerBuilder.assembleAvailableAnswers(b)

    assert(parsedAnswerOptions._1.isEmpty == false && parsedAnswerOptions._2.isEmpty ==  true)

    parsedAnswerOptions._1.get.humanReadableAnswerText === "subjective answer"

  }

  test("AnswerOptions in a JSON string with both subjective and objective answers - as available in DB - is parsed as expected") {


    val sampleAnswerOption = "{\"subjective\":\"subjective answer\",\"objective\":{\"options\":[{\"option1\":\"N\"},{\"option2\":\"N\"},{\"option3\":\"Y\"},{\"option4\":\"N\"}]},\"total_options\":4,\"correct_options\":2}}"


    val b = AnswerBuilder.unJasonifyAnswers(sampleAnswerOption)
    val parsedAnswerOptions = AnswerBuilder.assembleAvailableAnswers(b)

    assert(parsedAnswerOptions._1.isEmpty == false && parsedAnswerOptions._2.isEmpty ==  false)

    parsedAnswerOptions._1.get.humanReadableAnswerText === "subjective answer"

    forAll(parsedAnswerOptions._2.get) {
      ans => assert(ans.isInstanceOf[PossibleAnswer] &&
        (ans.isInstanceOf[ObjectiveIncorrectAnswer] || ans.isInstanceOf[ObjectiveCorrectAnswer]))
    }

    assert(parsedAnswerOptions._2.get.toIndexedSeq(1).humanReadableAnswerText === "option2")
    assert(parsedAnswerOptions._2.get.toIndexedSeq(2) === ObjectiveCorrectAnswer("option3"))
  }


  test("AnswerOptions in a malformed JSON string is not parsed. The hint is provided as expected") {


    val sampleAnswerOption = "{\"subjective\":\"subjective answer\",\"objective\":{\"options\":[\"option1\":\"N\"},\"option2\":\"N\"},{\"option3\":\"Y\"},{\"option4\":\"N\"}]},\"total_options\":4,\"correct_options\":2}}"


    val b = AnswerBuilder.unJasonifyAnswers(sampleAnswerOption)

    assert(b.isInstanceOf[FailedToParseAnswersAvailableInDB] === true)
    assert(b.asInstanceOf[FailedToParseAnswersAvailableInDB].subjective.isEmpty === false)
    assert(b.asInstanceOf[FailedToParseAnswersAvailableInDB].subjective.get.contains("{\"subjective\":\"") === true)
  }


}
