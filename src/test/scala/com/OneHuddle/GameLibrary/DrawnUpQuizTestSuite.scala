package com.OneHuddle.GameLibrary


import com.OneHuddle.Quiz.QuizDealer
import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, DisplayableObjectiveQuestionAndAnswer, DisplayableSubjectiveQuestionAndAnswer, ObjectiveAnswer, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, ReadableAnswerWithCorrectnessIndicated}
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfterAll, FunSuite, MustMatchers}

/**
  * Created by nirmalya on 4/6/18.
  */
class DrawnUpQuizTestSuite extends FunSuite
  with MustMatchers
  with BeforeAndAfterAll {

  implicit val f = org.json4s.DefaultFormats
  implicit val formats_2 = Serialization.formats(
    ShortTypeHints(
      List(
        classOf[AnswersAvailableInDB],
        classOf[DisplayableSubjectiveQuestionAndAnswer],
        classOf[DisplayableObjectiveQuestionAndAnswer],
        classOf[ReadableAnswerWithCorrectnessIndicated]
      )
    )
  )

  val subjectiveQuestionAndAnswerSet =
    List(

      RawQuestionAnswerScoreTriple("Who won the FIFA WC in 2002?","{\"subjective\":\"Brazil\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("When did India qualify for the world cup last time?","{\"subjective\":\"1950\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("Where will the finals of WC 2018 be held?","{\"subjective\":\"Moscow,Russia\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",300),
      RawQuestionAnswerScoreTriple("Where in Africa were the finals of FIFA WC held first time?","{\"subjective\":\"Republic of South Africa\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",400)
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
      RawQuestionAnswerScoreTriple("Where in Africa were the finals of FIFA WC held first time?","{\"subjective\":null,\"objective\":{\"options\":[{\"Egypt\":\"N\"},{\"South Africa\":\"Y\"},{\"Ghana\":\"N\"},{\"Tunisia\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("Which countries host the World Cup in 2002?","{\"subjective\":null,\"objective\":{\"options\":[{\"England and Ireland\":\"N\"},{\"Japan and South Korea\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )

  val questAndAnsProcessedCategoryIndia =
    List(
      RawQuestionAnswerScoreTriple("What is the capital of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"New Delhi\":\"Y\"},{\"Mumbai\":\"N\"},{\"Chennai\":\"N\"},{\"Calcutta\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",100),
      RawQuestionAnswerScoreTriple("When did India achieve her indepdence?","{\"subjective\":null,\"objective\":{\"options\":[{\"1977\":\"N\"},{\"1967\":\"N\"},{\"1947\":\"Y\"},{\"1997\":\"N\"},{\"2007\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("When did India become a republic?","{\"subjective\":null,\"objective\":{\"options\":[{\"1977\":\"N\"},{\"1967\":\"N\"},{\"1950\":\"Y\"},{\"1957\":\"N\"},{\"2007\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
      RawQuestionAnswerScoreTriple("Which of the following is Tier-I city in India","{\"subjective\":null,\"objective\":{\"options\":[{\"Bangalore\":\"Y\"},{\"Calcutta\":\"Y\"},{\"Mumbai\":\"Y\"},{\"Chennai\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",300),
      RawQuestionAnswerScoreTriple("What is the National Animal of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Lion\":\"N\"},{\"Royal Bengal Tiger\":\"Y\"},{\"Elephant\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("Which is the longest river in India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Brahmaputra\":\"N\"},{\"Narmada\":\"N\"},{\"Ganga\":\"Y\"},{\"Kaveri\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400),
      RawQuestionAnswerScoreTriple("What is the National Bird of India?","{\"subjective\":null,\"objective\":{\"options\":[{\"Myna\":\"N\"},{\"Eagle\":\"N\"},{\"Swan\":\"N\"},{\"Crow\":\"N\"},{\"Parakeet\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",500)
    )



  def dummyQuesRandomizer  (l: List[QuestionAnswerPairPack]): List[QuestionAnswerPairPack] = l
  def dummyAnsRandomizer   (l: List[ObjectiveAnswer]):        List[ObjectiveAnswer]        = l

  val shelfIDForSoccerUSEnglish = GameLibraryShelfID(1001,"Soccer","en_US")
  val shelfIDForIndiaUSEnglish  = GameLibraryShelfID(1002,"India","en_us")
  val shelfIDForWorldHistoryUSEnglish  = GameLibraryShelfID(1003,"World History","en_us")
  val shelfIDForSubjectiveAnwers  = GameLibraryShelfID(1003,"Subjective Answers","en_us")

  val library =
    GameLibrary(
      dummyQuesRandomizer,
      dummyAnsRandomizer
    )
      .attachAShelf(shelfIDForSoccerUSEnglish,       questAndAnsProcessedCategorySoccer)
      .attachAShelf(shelfIDForIndiaUSEnglish,        questAndAnsProcessedCategoryIndia)
      .attachAShelf(shelfIDForWorldHistoryUSEnglish, questAndAnsProcessedCategoryWorldHistory)
      .attachAShelf(shelfIDForSubjectiveAnwers,      subjectiveQuestionAndAnswerSet)

  test ("Given a correct shelfID, the contents of a subjective quiz drawn are found as expected") {

    implicit val f = org.json4s.DefaultFormats

    val quizButler = new QuizDealer(library)

      val producedSubjectives = quizButler.dealASubjectiveQuiz(shelfIDForSubjectiveAnwers, 4)

      val expectedSubjectives = IndexedSeq(
        """{"marks":100,"question":"Who won the FIFA WC in 2002?","answer":"Brazil"}""",
        """{"marks":200,"question":"When did India qualify for the world cup last time?","answer":"1950"}""",
        """{"marks":300,"question":"Where will the finals of WC 2018 be held?","answer":"Moscow,Russia"}""",
        """{"marks":400,"question":"Where in Africa were the finals of FIFA WC held first time?","answer":"Republic of South Africa"}"""
      )

      assert(compact(producedSubjectives(0).toJason) === expectedSubjectives(0))
      assert(compact(producedSubjectives(1).toJason) === expectedSubjectives(1))
      assert(compact(producedSubjectives(2).toJason) === expectedSubjectives(2))
      assert(compact(producedSubjectives(3).toJason) === expectedSubjectives(3))
  }

  test ("Given an incorrect shelfID, the contents of a subjective quiz correctly say what the reason of failure is") {

    implicit val f = org.json4s.DefaultFormats

    val quizButler = new QuizDealer(library)

    val producedSubjectives = quizButler.dealASubjectiveQuiz(GameLibraryShelfID(1000,"Soccer","en-US"),4 )

    assert(producedSubjectives.length === 1)

    val expectedSubjectives = IndexedSeq(
      """{"marks":-1,"question":"Incorrect shelfID","answer":"Incorrect shelfID"}"""
    )

    assert(compact(producedSubjectives(0).toJason) === expectedSubjectives(0))
  }

  test ("Given a correct shelfID, the contents of an objective quiz is formed as expected") {

    implicit val f = org.json4s.DefaultFormats

    val quizButler = new QuizDealer(library)

    val producedObjectives = quizButler.dealAnObjectiveQuiz(shelfIDForSoccerUSEnglish,numQuestionsReqd = 4,optionsPerAnswer = 4)

    assert(producedObjectives.length === 4)

    val expectedObjectives = IndexedSeq(
      """{"marks":100,"question":"Who won the FIFA WC in 2002?","answers":[{"text":"England","isCorrect":"N"},{"text":"India","isCorrect":"N"},{"text":"Brazil","isCorrect":"Y"},{"text":"Germany","isCorrect":"N"}]}""",
      """{"marks":200,"question":"When did India qualify for the world cup last time?","answers":[{"text":"1978","isCorrect":"N"},{"text":"1964","isCorrect":"N"},{"text":"1950","isCorrect":"Y"},{"text":"1990","isCorrect":"N"}]}""",
      """{"marks":300,"question":"Where will the finals of WC 2018 be held?","answers":[{"text":"India","isCorrect":"N"},{"text":"Mexico","isCorrect":"N"},{"text":"Russia","isCorrect":"Y"},{"text":"Germany","isCorrect":"N"}]}""",
      """{"marks":400,"question":"Where in Africa were the finals of FIFA WC held first time?","answers":[{"text":"Egypt","isCorrect":"N"},{"text":"South Africa","isCorrect":"Y"},{"text":"Ghana","isCorrect":"N"},{"text":"Tunisia","isCorrect":"N"}]}"""
    )

    assert(compact(producedObjectives(0).toJson) === expectedObjectives(0))
    assert(compact(producedObjectives(1).toJson) === expectedObjectives(1))
    assert(compact(producedObjectives(2).toJson) === expectedObjectives(2))
    assert(compact(producedObjectives(3).toJson) === expectedObjectives(3))

    print(pretty(producedObjectives(0).toJson))

  }
}
