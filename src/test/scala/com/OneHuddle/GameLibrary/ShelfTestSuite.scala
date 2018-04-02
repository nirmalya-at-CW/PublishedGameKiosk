package com.OneHuddle.GameLibrary

import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID, LibraryUtilities}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AnswersAvailableInDB, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, PossibleAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, SubjectiveCorrectAnswer}
import org.json4s.ShortTypeHints
import org.json4s.jackson.Serialization
import org.scalatest.{BeforeAndAfterAll, FunSuite, MustMatchers}
import org.scalatest.Matchers._

/**
  * Created by nirmalya on 3/23/18.
  */
class ShelfTestSuite  extends FunSuite
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

  def dummyQuesRandomizer (l: List[QuestionAnswerPairPack]): List[QuestionAnswerPairPack] = l
  def dummyAnsRandomizer   (l: List[ObjectiveAnswer]):        List[ObjectiveAnswer]  = l

  test("Given a bunch of processed (not raw from DB) subjective questions and answers for a given Game, shelves must be organized properly") {


      val questAndAnsProcessed =
        List(

          RawQuestionAnswerScoreTriple("Who won the FIFA WC in 2002?","{\"subjective\":\"Brazil\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",100),
          RawQuestionAnswerScoreTriple("When did India qualify for the world cup last time?","{\"subjective\":\"1950\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",200),
          RawQuestionAnswerScoreTriple("Where will the finals of WC 2018 be held?","{\"subjective\":\"Moscow,Russia\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",300),
          RawQuestionAnswerScoreTriple("Where in Africa were the finals of FIFA WC held first time?","{\"subjective\":\"Republic of South Africa\",\"objective\":null,\"total_options\":0,\"correct_options\":0}}",400)
        )

      val shelfIDForSoccer = GameLibraryShelfID(1001,"soccer","en_US")

      val library =
        GameLibrary(
          dummyQuesRandomizer,
          dummyAnsRandomizer
        )
        .arrangeAShelf(shelfIDForSoccer,questAndAnsProcessed)

      assert(library.reachShelf(shelfIDForSoccer).isEmpty == false)
      assert(library.reachShelf(shelfIDForSoccer).map(e => e.contents.length).get == 4)

      assert(library.reachShelf(shelfIDForSoccer).get.contents.contains(
        QuestionAnswerPairPack(
          Question("Who won the FIFA WC in 2002?", 100),
          ansSubjective = Some(SubjectiveCorrectAnswer("Brazil")),
          ansObjective = None)
      ) == true)

      assert(library.reachShelf(shelfIDForSoccer).get.contents.contains (
        QuestionAnswerPairPack(
          Question("When did India qualify for the world cup last time?", 200),
          ansSubjective = Some(SubjectiveCorrectAnswer("1950")),
          ansObjective = None)
        ) == true)

      assert(library.reachShelf(shelfIDForSoccer).get.contents.contains(
        QuestionAnswerPairPack(
          Question("Where will the finals of WC 2018 be held?", 300),
          ansSubjective = Some(SubjectiveCorrectAnswer("Moscow,Russia")),
          ansObjective = None)
        )  == true)

      assert(library.reachShelf(shelfIDForSoccer).get.contents.contains (
        QuestionAnswerPairPack(
          Question("Where in Africa were the finals of FIFA WC held first time?", 400),
          ansSubjective = Some(SubjectiveCorrectAnswer("Republic of South Africa")),
          ansObjective = None)
        )  == true)

      library.reachShelf(shelfIDForSoccer).get.questionsByScore(100).length should be (1)
      library.reachShelf(shelfIDForSoccer).get.questionsByScore(200).length should be (1)
      library.reachShelf(shelfIDForSoccer).get.questionsByScore(300).length should be (1)
      library.reachShelf(shelfIDForSoccer).get.questionsByScore(400).length should be (1)
      library.reachShelf(shelfIDForSoccer).get.questionsByScore.get(500).isEmpty should be (true)
  }

  test("Given a bunch of processed (not raw from DB) objective questions and answers for a given Game, shelves must be organized properly") {


    val questAndAnsProcessed =
      List(
        // "{\"subjective\":null,\"objective\":{\"options\":[{\"option1\":\"N\"},{\"option2\":\"N\"},{\"option3\":\"Y\"},{\"option4\":\"N\"}]},\"total_options\":4,\"correct_options\":2}}"
        RawQuestionAnswerScoreTriple("Who won the FIFA WC in 2002?","{\"subjective\":null,\"objective\":{\"options\":[{\"England\":\"N\"},{\"India\":\"N\"},{\"Brazil\":\"Y\"},{\"Germany\":\"N\"},{\"Spain\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",100),
        RawQuestionAnswerScoreTriple("When did India qualify for the world cup last time?","{\"subjective\":null,\"objective\":{\"options\":[{\"1978\":\"N\"},{\"1964\":\"N\"},{\"1950\":\"Y\"},{\"1990\":\"N\"},{\"2010\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",200),
        RawQuestionAnswerScoreTriple("Where will the finals of WC 2018 be held?","{\"subjective\":null,\"objective\":{\"options\":[{\"India\":\"N\"},{\"Mexico\":\"N\"},{\"Russia\":\"Y\"}]},\"total_options\":0,\"correct_options\":0}}",300),
        RawQuestionAnswerScoreTriple("Where in Africa were the finals of FIFA WC held first time?","{\"subjective\":null,\"objective\":{\"options\":[{\"Egypt\":\"N\"},{\"South Africa\":\"Y\"},{\"Ghana\":\"N\"}]},\"total_options\":0,\"correct_options\":0}}",400)
      )

    val shelfIDForSoccer = GameLibraryShelfID(1001,"soccer","en_US")

    val library =
      GameLibrary(
        dummyQuesRandomizer,
        dummyAnsRandomizer
      )
      .arrangeAShelf(shelfIDForSoccer,questAndAnsProcessed)

    assert(library.reachShelf(shelfIDForSoccer).isEmpty == false)
    assert(library.reachShelf(shelfIDForSoccer).map(e => e.contents.length).get == 4)

    assert(library.reachShelf(shelfIDForSoccer).get.contents.contains(
      QuestionAnswerPairPack(
        Question("Who won the FIFA WC in 2002?", 100),
        ansSubjective = None,
        ansObjective = Some(List(ObjectiveIncorrectAnswer("England"),ObjectiveIncorrectAnswer("India"),ObjectiveCorrectAnswer("Brazil"),ObjectiveIncorrectAnswer("Germany"),ObjectiveIncorrectAnswer("Spain")))
      )) == true)

    assert(library.reachShelf(shelfIDForSoccer).get.contents.contains (
      QuestionAnswerPairPack(
        Question("When did India qualify for the world cup last time?", 200),
        ansSubjective = None,
        ansObjective = Some(List(ObjectiveIncorrectAnswer("1978"),ObjectiveIncorrectAnswer("1964"),ObjectiveCorrectAnswer("1950"),ObjectiveIncorrectAnswer("1990"),ObjectiveIncorrectAnswer("2010"))))
      ) == true)

    assert(library.reachShelf(shelfIDForSoccer).get.contents.contains(
      QuestionAnswerPairPack(
        Question("Where will the finals of WC 2018 be held?", 300),
        ansSubjective = None,
        ansObjective = Some(List(ObjectiveIncorrectAnswer("India"),ObjectiveIncorrectAnswer("Mexico"),ObjectiveCorrectAnswer("Russia"))))
    )  == true)

    assert(library.reachShelf(shelfIDForSoccer).get.contents.contains (
      QuestionAnswerPairPack(
        Question("Where in Africa were the finals of FIFA WC held first time?", 400),
        ansSubjective = None,
        ansObjective = Some(List(ObjectiveIncorrectAnswer("Egypt"),ObjectiveCorrectAnswer("South Africa"),ObjectiveIncorrectAnswer("Ghana"))))
    )  == true)

    library.reachShelf(shelfIDForSoccer).get.questionsByScore(100).length should be (1)
    library.reachShelf(shelfIDForSoccer).get.questionsByScore(200).length should be (1)
    library.reachShelf(shelfIDForSoccer).get.questionsByScore(300).length should be (1)
    library.reachShelf(shelfIDForSoccer).get.questionsByScore(400).length should be (1)
    library.reachShelf(shelfIDForSoccer).get.questionsByScore.get(500).isEmpty should be (true)

    }


}
