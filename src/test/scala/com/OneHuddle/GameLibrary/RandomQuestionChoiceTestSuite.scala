package com.OneHuddle.GameLibrary

import com.OneHuddle.Quiz.{OfferedObjectiveQuestionAndAnswerForQuiz, QuizButler}
import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple}
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfterAll, FunSuite, MustMatchers}

/**
  * Created by nirmalya on 4/3/18.
  */
class RandomQuestionChoiceTestSuite extends FunSuite
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
      RawQuestionAnswerScoreTriple("Which year did the Bolshevik Revolution take place in Russia","{\"subjective\":null,\"objective\":{\"options\":[{\"1817\":\"N\"},{\"1617\":\"N\"},{\"1717\":\"N\"},{\"1917\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("Which of these countries belong to Great Britain?","{\"subjective\":null,\"objective\":{\"options\":[{\"Wales\":\"Y\"},{\"Northern Ireland\":\"Y\"},{\"Scotland\":\"Y\"},{\"English\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("How many states does United States of America have?","{\"subjective\":null,\"objective\":{\"options\":[{\"49\":\"N\"},{\"42\":\"N\"},{\"50\":\"Y\"},{\"51\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("In which city is the Eiffel Tower located?","{\"subjective\":null,\"objective\":{\"options\":[{\"London\":\"N\"},{\"New York\":\"N\"},{\"Lima\":\"N\"},{\"Seoul\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )

  val questAndAnsProcessedCategorySimpleMaths =
    List(
      RawQuestionAnswerScoreTriple("What is 1+1 = ?","{\"subjective\":null,\"objective\":{\"options\":[{\"3\":\"N\"},{\"4\":\"N\"},{\"5\":\"N\"},{\"0\":\"N\"},{\"2\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("What are the missing numbers in series 1,2,3,6,7,8?","{\"subjective\":null,\"objective\":{\"options\":[{\"4\":\"N\"},{\"5\":\"N\"},{\"6\":\"N\"},{\"4 and 5\":\"Y\"},{\"0\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("What is the square root of 256?","{\"subjective\":null,\"objective\":{\"options\":[{\"4\":\"N\"},{\"8\":\"N\"},{\"128\":\"N\"},{\"256\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("What is the GCD of 9 and 21?","{\"subjective\":null,\"objective\":{\"options\":[{\"3\":\"Y\"},{\"9\":\"N\"},{\"18\":\"N\"},{\"1\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("What is factorial of 4?","{\"subjective\":null,\"objective\":{\"options\":[{\"16\":\"N\"},{\"8\":\"N\"},{\"24\":\"Y\"},{\"4\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("What is the square root of -1 called","{\"subjective\":null,\"objective\":{\"options\":[{\"Natural Number\":\"N\"},{\"Unnatural Number\":\"N\"},{\"Perfect Number\":\"N\"},{\"Imaginary Number\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )

  def dummyQuesRandomizer  (l: List[QuestionAnswerPairPack]): List[QuestionAnswerPairPack] = l
  def dummyAnsRandomizer   (l: List[ObjectiveAnswer]):        List[ObjectiveAnswer]        = l

  val shelfIDForWorldHistoryUSEnglish  = GameLibraryShelfID(1003,"World History","en_us")
  val shelfIDForSimpleMathsUSEnglish  = GameLibraryShelfID(1004,"Simple Maths","en_us")

  val library =
    GameLibrary(
      dummyQuesRandomizer,
      dummyAnsRandomizer
    )
      .arrangeAShelf(shelfIDForWorldHistoryUSEnglish, questAndAnsProcessedCategoryWorldHistory)
      .arrangeAShelf(shelfIDForSimpleMathsUSEnglish, questAndAnsProcessedCategorySimpleMaths)

  test("When a score bucket is missing from question-set, a random question fills in its place correctly") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForWorldHistoryUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(5,4,shelf)

    val ordered =
      offered
        .fillInScoreBuckets
        .fillInLeftOverEmptyScoreBuckets
        .ensureCorrectAnswerIsEmbeddedAsNecessary
        .arrangeInOrderOfScores

    assert(ordered.length === 5)

    assert(ordered(2)._1 === 300)
    assert(ordered(2)._2.isEmpty === false)
    assert(ordered(2)._2.get._1.humanReadableQuestionText  === "Which year did the Bolshevik Revolution take place in Russia")

  }

  test("When more than 1 random questions are to be selected, the buckets are filled in correctly") {

    val quizButler = new QuizButler(library)

    val shelf = library.reachShelf(shelfIDForSimpleMathsUSEnglish).get

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
    assert(ordered(0)._2.get._1.humanReadableQuestionText  === "What is 1+1 = ?")

    assert(ordered(1)._1 === 200)
    assert(ordered(1)._2.isEmpty === false)
    assert(ordered(1)._2.get._1.humanReadableQuestionText  === "What are the missing numbers in series 1,2,3,6,7,8?")

    assert(ordered(2)._1 === 300)
    assert(ordered(2)._2.isEmpty === false)
    assert(ordered(2)._2.get._1.humanReadableQuestionText  === "What is the square root of 256?")

    assert(ordered(3)._1 === 400)
    assert(ordered(3)._2.isEmpty === false)
    assert(ordered(3)._2.get._1.humanReadableQuestionText  === "What is the GCD of 9 and 21?")



  }
}
