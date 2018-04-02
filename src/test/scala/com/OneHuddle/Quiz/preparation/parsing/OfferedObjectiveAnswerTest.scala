package com.OneHuddle.Quiz.preparation.parsing

import com.OneHuddle.Quiz.{OfferedObjectiveQuestionAndAnswerForQuiz, QuizButler}
import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID, LibraryUtilities, QnAShelf}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, NoneOfTheAboveObjectiveAnswer, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, PossibleAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple}
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfterAll, FunSuite, MustMatchers}

/**
  * Created by nirmalya on 3/27/18.
  */
class OfferedObjectiveAnswerTest extends FunSuite
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


  val questAndAnsProcessedCategoryWorldHistory =
    List(
      RawQuestionAnswerScoreTriple("Which year did USA gain independence?","{\"subjective\":null,\"objective\":{\"options\":[{\"1976\":\"N\"},{\"1876\":\"N\"},{\"2076\":\"N\"},{\"1666\":\"N\"},{\"1776\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("Which the smallest country in the world?","{\"subjective\":null,\"objective\":{\"options\":[{\"Malta\":\"N\"},{\"Mauritius\":\"N\"},{\"Sri Lanka\":\"N\"},{\"Hong Kong\":\"N\"},{\"Vatican City\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("Which year did the Bolshevik Revolution took place in Russia","{\"subjective\":null,\"objective\":{\"options\":[{\"1817\":\"N\"},{\"1617\":\"N\"},{\"1717\":\"N\"},,{\"1917\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("Which of these countries belong to Great Britain?","{\"subjective\":null,\"objective\":{\"options\":[{\"Wales\":\"Y\"},{\"Northern Ireland\":\"Y\"},{\"Scotland\":\"Y\"},{\"English\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("How many states does United States of America have?","{\"subjective\":null,\"objective\":{\"options\":[{\"49\":\"50\"},{\"42\":\"N\"},{\"50\":\"Y\"},{\"51\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("In which city is the Eiffel Tower located?","{\"subjective\":null,\"objective\":{\"options\":[{\"London\":\"N\"},{\"New York\":\"N\"},{\"Lima\":\"N\"},{\"Seoul\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )


  val questAndAnsProcessedCategorySoccer =
    List(
      RawQuestionAnswerScoreTriple("Who won the FIFA WC in 2002?","{\"subjective\":null,\"objective\":{\"options\":[{\"England\":\"N\"},{\"India\":\"N\"},{\"Brazil\":\"Y\"},{\"Germany\":\"N\"},{\"Spain\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("When did India qualify for the world cup last time?","{\"subjective\":null,\"objective\":{\"options\":[{\"1978\":\"N\"},{\"1964\":\"N\"},{\"1950\":\"Y\"},{\"1990\":\"N\"},{\"2010\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("Where will the finals of WC 2018 be held?","{\"subjective\":null,\"objective\":{\"options\":[{\"India\":\"N\"},{\"Mexico\":\"N\"},{\"Russia\":\"Y\"},{\"Germany\":\"N\"}]},\"total_options\":0,\"correct_options\":0}]}",300),
      RawQuestionAnswerScoreTriple("Where in Africa were the finals of FIFA WC held first time?","{\"subjective\":null,\"objective\":{\"options\":[{\"Egypt\":\"N\"},{\"South Africa\":\"Y\"},{\"Ghana\":\"N\"},{\"Tunisia\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400)
    )

  val questAndAnsProcessedCategoryIndia =
    List(
      RawQuestionAnswerScoreTriple("What is the capital of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"New Delhi\":\"Y\"},{\"Mumbai\":\"N\"},{\"Chennai\":\"N\"},{\"Calcutta\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("When did India achieve her indepdence?","{\"subjective\":null,\"objective\":{\"options\":[{\"1977\":\"N\"},{\"1967\":\"N\"},{\"1947\":\"Y\"},{\"1997\":\"N\"},{\"2007\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("When did India become a republic?","{\"subjective\":null,\"objective\":{\"options\":[{\"1977\":\"N\"},{\"1967\":\"N\"},{\"1950\":\"Y\"},{\"1957\":\"N\"},{\"2007\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("Where is the financial capital of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Bangalore\":\"N\"},{\"Calcutta\":\"N\"},{\"Mumbai\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",300),
      RawQuestionAnswerScoreTriple("What is the National Animal of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Lion\":\"N\"},{\"Royal Bengal Tiger\":\"Y\"},{\"Elephant\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("Which is the longest river in India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Brahmaputra\":\"N\"},{\"Narmada\":\"N\"},{\"Ganga\":\"Y\"},{\"Kaveri\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("What is the National Bird of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Myna\":\"N\"},{\"Eagle\":\"N\"},{\"Swan\":\"N\"},{\"Crow\":\"N\"},{\"Parakeet\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )

  def dummyQuesRandomizer  (l: List[QuestionAnswerPairPack]): List[QuestionAnswerPairPack] = l
  def dummyAnsRandomizer   (l: List[ObjectiveAnswer]):        List[ObjectiveAnswer]        = l

  val shelfIDForSoccerUSEnglish = GameLibraryShelfID(1001,"Soccer","en_US")
  val shelfIDForIndiaUSEnglish  = GameLibraryShelfID(1002,"India","en_us")
  val shelfIDForWorldHistoryUSEnglish  = GameLibraryShelfID(1003,"World History","en_us")

  val library =
    GameLibrary(
      dummyQuesRandomizer,
      dummyAnsRandomizer
    )
    .arrangeAShelf(shelfIDForSoccerUSEnglish,       questAndAnsProcessedCategorySoccer)
    .arrangeAShelf(shelfIDForIndiaUSEnglish,        questAndAnsProcessedCategoryIndia)
    .arrangeAShelf(shelfIDForWorldHistoryUSEnglish, questAndAnsProcessedCategoryWorldHistory)

  test("Right order of answers is formed for marks (100), when 1 question is asked for, from the library") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForSoccerUSEnglish).get

    val offered_1 = OfferedObjectiveQuestionAndAnswerForQuiz(1,4,shelf)

    val ordered_1 = offered_1.fillInScoreBuckets
      .fillInLeftOverEmptyScoreBuckets
      .arrangeInOrderOfScores

    assert(ordered_1.length === 1)
    assert(ordered_1(0)._1 === 100)
    assert(ordered_1(0)._2.isEmpty === false)
    assert(ordered_1(0)._2.get._1.humanReadableQuestionText === "Who won the FIFA WC in 2002?")

    assert(ordered_1(0)._2.get._2.length === 4)
    assert(ordered_1(0)._2.get._2.toIndexedSeq(0).isInstanceOf[ObjectiveIncorrectAnswer] === true)
    assert(ordered_1(0)._2.get._2.toIndexedSeq(0).humanReadableAnswerText === "England")

    assert(ordered_1(0)._2.get._2.toIndexedSeq(2).isInstanceOf[ObjectiveCorrectAnswer] === true)
    assert(ordered_1(0)._2.get._2.toIndexedSeq(2).humanReadableAnswerText === "Brazil")
  }

  test("Right order of answers is formed for marks (100,200,300,400), when 4 questions are asked for, from the library") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForSoccerUSEnglish).get

    val offered_2 = OfferedObjectiveQuestionAndAnswerForQuiz(4,4,shelf)

    val ordered_2 = offered_2.fillInScoreBuckets
      .fillInLeftOverEmptyScoreBuckets
      .arrangeInOrderOfScores

    assert(ordered_2.length === 4)

   assert(ordered_2(0)._1 === 100)
    assert(ordered_2(0)._2.isEmpty === false)
    assert(ordered_2(0)._2.get._1.humanReadableQuestionText === "Who won the FIFA WC in 2002?")
    assert(ordered_2(0)._2.get._2.length === 4)
    assert(ordered_2(0)._2.get._2.contains(ObjectiveIncorrectAnswer("England"))    === true)
    assert(ordered_2(0)._2.get._2.contains(ObjectiveIncorrectAnswer("India"))      === true)
    assert(ordered_2(0)._2.get._2.contains(ObjectiveIncorrectAnswer("Germany"))    === true)
    assert(ordered_2(0)._2.get._2.contains(ObjectiveCorrectAnswer("Brazil"))       === true)


    assert(ordered_2(1)._1 === 200)
    assert(ordered_2(1)._2.isEmpty === false)
    assert(ordered_2(1)._2.get._1.humanReadableQuestionText === "When did India qualify for the world cup last time?")
    assert(ordered_2(1)._2.get._2.length === 4)
    assert(ordered_2(1)._2.get._2.contains(ObjectiveIncorrectAnswer("1978"))      === true)
    assert(ordered_2(1)._2.get._2.contains(ObjectiveIncorrectAnswer("1964"))      === true)
    assert(ordered_2(1)._2.get._2.contains(ObjectiveIncorrectAnswer("1990"))      === true)
    assert(ordered_2(1)._2.get._2.contains(ObjectiveCorrectAnswer("1950"))        === true)


    assert(ordered_2(2)._1 === 300)
    assert(ordered_2(2)._2.isEmpty === false)
    assert(ordered_2(2)._2.get._1.humanReadableQuestionText === "Where will the finals of WC 2018 be held?")
    assert(ordered_2(2)._2.get._2.length === 4)
    assert(ordered_2(2)._2.get._2.contains(ObjectiveIncorrectAnswer("India"))      === true)
    assert(ordered_2(2)._2.get._2.contains(ObjectiveIncorrectAnswer("Mexico"))     === true)
    assert(ordered_2(2)._2.get._2.contains(ObjectiveIncorrectAnswer("Germany"))    === true)
    assert(ordered_2(2)._2.get._2.contains(ObjectiveCorrectAnswer("Russia"))       === true)


    assert(ordered_2(3)._1 === 400)
    assert(ordered_2(3)._2.isEmpty === false)
    assert(ordered_2(3)._2.get._1.humanReadableQuestionText === "Where in Africa were the finals of FIFA WC held first time?")
    assert(ordered_2(3)._2.get._2.length === 4)
    assert(ordered_2(3)._2.get._2.contains(ObjectiveIncorrectAnswer("Egypt"))          === true)
    assert(ordered_2(3)._2.get._2.contains(ObjectiveCorrectAnswer("South Africa"))     === true)
    assert(ordered_2(3)._2.get._2.contains(ObjectiveIncorrectAnswer("Ghana"))          === true)
    assert(ordered_2(3)._2.get._2.contains(ObjectiveIncorrectAnswer("Tunisia"))        === true)


  }

  test("When multiple questions of same marks exist, only one of them is selected") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForIndiaUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(4,4,shelf)

    val ordered =
      offered
      .fillInScoreBuckets
      .fillInLeftOverEmptyScoreBuckets
      .ensureCorrectAnswerIsEmbeddedAsNecessary
      .arrangeInOrderOfScores

    assert(ordered.length === 4)

    assert(ordered(0)._1 === 100)
    assert(ordered(0)._2.isEmpty === false)
    assert(ordered(0)._2.get._1.humanReadableQuestionText  === "What is the capital of India?")

    // There is another Q of score 200 in the testset after this Q, but only this Q should be chosen because we are not randomizing while testing.
    assert(ordered(1)._1 === 200)
    assert(ordered(1)._2.isEmpty === false)
    assert(ordered(1)._2.get._1.humanReadableQuestionText  === "When did India achieve her indepdence?")

    assert(ordered(2)._1 === 300)
    assert(ordered(2)._2.isEmpty === false)
    assert(ordered(2)._2.get._1.humanReadableQuestionText  === "Where is the financial capital of India?")

    // There is another Q of score 400 in the testset after this Q, but only this Q should be chosen because we are not randomizing while testing.
    assert(ordered(3)._1 === 400)
    assert(ordered(3)._2.isEmpty === false)
    assert(ordered(3)._2.get._1.humanReadableQuestionText  === "What is the National Animal of India?")

  }

  test("When an answer has all incorrect options, an option for none of the above must be offered") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForIndiaUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(5,4,shelf)

    val ordered =
      offered
        .fillInScoreBuckets
        .fillInLeftOverEmptyScoreBuckets
        .ensureCorrectAnswerIsEmbeddedAsNecessary
        .arrangeInOrderOfScores

    assert(ordered.length === 5)

    assert(ordered(4)._1 === 500)
    assert(ordered(4)._2.isEmpty === false)
    assert(ordered(4)._2.get._1.humanReadableQuestionText  === "What is the National Bird of India?")

    val allAnswers = ordered(4)._2.get._2.toIndexedSeq

    assert(allAnswers(0) === ObjectiveIncorrectAnswer("Myna"))
    assert(allAnswers(1) === ObjectiveIncorrectAnswer("Eagle"))
    assert(allAnswers(2) === ObjectiveIncorrectAnswer("Swan"))
    assert(allAnswers(3) === NoneOfTheAboveObjectiveAnswer)

  }

  test("When an answer has all incorrect options, an option for none of the above must be offered") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForIndiaUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(5,4,shelf)

    val ordered =
      offered
        .fillInScoreBuckets
        .fillInLeftOverEmptyScoreBuckets
        .ensureCorrectAnswerIsEmbeddedAsNecessary
        .arrangeInOrderOfScores

    assert(ordered.length === 5)

    assert(ordered(4)._1 === 500)
    assert(ordered(4)._2.isEmpty === false)
    assert(ordered(4)._2.get._1.humanReadableQuestionText  === "What is the National Bird of India?")

    val allAnswers = ordered(4)._2.get._2.toIndexedSeq

    assert(allAnswers(0) === ObjectiveIncorrectAnswer("Myna"))
    assert(allAnswers(1) === ObjectiveIncorrectAnswer("Eagle"))
    assert(allAnswers(2) === ObjectiveIncorrectAnswer("Swan"))
    assert(allAnswers(3) === NoneOfTheAboveObjectiveAnswer)

  }


}
