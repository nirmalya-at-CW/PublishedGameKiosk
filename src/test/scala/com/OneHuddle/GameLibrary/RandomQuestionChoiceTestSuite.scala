package com.OneHuddle.GameLibrary

import com.OneHuddle.Quiz.{OfferedObjectiveQuestionAndAnswerForQuiz, QuizDealer}
import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple}
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, MustMatchers}
import org.scalactic.Explicitly._
import org.scalactic.StringNormalizations._

/**
  * Created by nirmalya on 4/3/18.
  */
class RandomQuestionChoiceTestSuite extends FunSuite
  with Matchers
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

  val questAndAnsProcessedCategorySimpleGeography =
    List(
      RawQuestionAnswerScoreTriple("What is the latitude at equator?","{\"subjective\":null,\"objective\":{\"options\":[{\"3\":\"N\"},{\"4\":\"N\"},{\"5\":\"N\"},{\"0\":\"Y\"},{\"2\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("How much time does earth take around itself once?","{\"subjective\":null,\"objective\":{\"options\":[{\"4h\":\"N\"},{\"5h\":\"N\"},{\"6h\":\"N\"},{\"24h\":\"Y\"},{\"0h\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("In which ocean or sea, one finds the Cape of Good Hope?","{\"subjective\":null,\"objective\":{\"options\":[{\"Pacific\":\"N\"},{\"Atlantic\":\"N\"},{\"North\":\"N\"},{\"Indian\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("In which country is the volcano Visuvius located?","{\"subjective\":null,\"objective\":{\"options\":[{\"Italy\":\"Y\"},{\"Spain\":\"N\"},{\"Greece\":\"N\"},{\"Turkey\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("Which country is called the Nippon?","{\"subjective\":null,\"objective\":{\"options\":[{\"South Korea\":\"N\"},{\"China\":\"N\"},{\"Japan\":\"Y\"},{\"HongKong\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",0),
      RawQuestionAnswerScoreTriple("Which island nation is called the Emerald Isle?","{\"subjective\":null,\"objective\":{\"options\":[{\"Mauritiaus\":\"N\"},{\"Macau\":\"N\"},{\"Secychelles\":\"N\"},{\"SriLanka\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",0)
    )

  def dummyQuesRandomizer  (l: List[QuestionAnswerPairPack]): List[QuestionAnswerPairPack] = l
  def dummyAnsRandomizer   (l: List[ObjectiveAnswer]):        List[ObjectiveAnswer]        = l

  val shelfIDForWorldHistoryUSEnglish     = GameLibraryShelfID(1003,"World History","en_us")
  val shelfIDForSimpleMathsUSEnglish      = GameLibraryShelfID(1004,"Simple Maths","en_us")
  val shelfIDForSimpleGeographyUSEnglish  = GameLibraryShelfID(1005,"Simple Geo","en_us")

  val library =
    GameLibrary(
      dummyQuesRandomizer,
      dummyAnsRandomizer
    )
      .arrangeAShelf(shelfIDForWorldHistoryUSEnglish,    questAndAnsProcessedCategoryWorldHistory)
      .arrangeAShelf(shelfIDForSimpleMathsUSEnglish,     questAndAnsProcessedCategorySimpleMaths)
      .arrangeAShelf(shelfIDForSimpleGeographyUSEnglish, questAndAnsProcessedCategorySimpleGeography)

  test("When a score bucket is missing from question-set, a random question fills in its place correctly") {

    val quizButler = new QuizDealer(library)

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

  test("When more than 1 random scorable questions are to be selected, the buckets are filled in correctly") {

    val quizButler = new QuizDealer(library)


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

  test("When only buckets 100 & 500 are provided (and rest are all random scorable questions), the buckets are filled in correctly") {

    val quizButler = new QuizDealer(library)

    val allZeroScoredQ = questAndAnsProcessedCategorySimpleMaths
      .filter(e => e.score == 0)
      .map(e => e.ques)


    val shelf = library.reachShelf(shelfIDForSimpleMathsUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(5,4,shelf)

    val ordered =
      offered
        .fillInScoreBuckets
        .fillInLeftOverEmptyScoreBuckets
        .ensureCorrectAnswerIsEmbeddedAsNecessary
        .arrangeInOrderOfScores

    assert(ordered.length === 5)

    assert(ordered(0)._1 === 100)
    assert(ordered(1)._1 === 200)
    assert(ordered(2)._1 === 300)
    assert(ordered(3)._1 === 400)
    assert(ordered(4)._1 === 500)

    assert(ordered(0)._2.isEmpty === false)
    assert(ordered(0)._2.get._1.humanReadableQuestionText  === "What is 1+1 = ?")
    assert(ordered(4)._2.isEmpty === false)
    assert(ordered(4)._2.get._1.humanReadableQuestionText  === "What is the square root of -1 called")

    assert(allZeroScoredQ.length == 4)
    allZeroScoredQ should contain allOf (
                                    ordered(1)._2.get._1.humanReadableQuestionText,
                                    ordered(2)._2.get._1.humanReadableQuestionText,
                                    ordered(3)._2.get._1.humanReadableQuestionText
    )


  }

  test("When all buckets are of random scorable questions and 5 questions are asked for, the buckets are filled in correctly") {

    val quizButler = new QuizDealer(library)

    val allZeroScoredQ = questAndAnsProcessedCategorySimpleGeography
      .filter(e => e.score == 0)
      .map(e => e.ques)


    val shelf = library.reachShelf(shelfIDForSimpleGeographyUSEnglish).get

    val offered = OfferedObjectiveQuestionAndAnswerForQuiz(5,4,shelf)

    val ordered =
      offered
        .fillInScoreBuckets
        .fillInLeftOverEmptyScoreBuckets
        .ensureCorrectAnswerIsEmbeddedAsNecessary
        .arrangeInOrderOfScores

    assert(ordered.length === 5)

    assert(ordered(0)._1 === 100)
    assert(ordered(1)._1 === 200)
    assert(ordered(2)._1 === 300)
    assert(ordered(3)._1 === 400)
    assert(ordered(4)._1 === 500)

    assert(allZeroScoredQ.length == 6)
    allZeroScoredQ should contain allOf (
      ordered(0)._2.get._1.humanReadableQuestionText,
      ordered(1)._2.get._1.humanReadableQuestionText,
      ordered(2)._2.get._1.humanReadableQuestionText,
      ordered(3)._2.get._1.humanReadableQuestionText,
      ordered(4)._2.get._1.humanReadableQuestionText
    )


  }
}
