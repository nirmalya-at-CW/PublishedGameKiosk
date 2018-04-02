package com.OneHuddle.Quiz.preparation

import com.OneHuddle.Quiz.preparation.parsing.AnswerBuilder
import QuizQuestionAnswerProtocol.{ObjectiveAnswer, PossibleAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, SubjectiveCorrectAnswer, UnavailableObjectiveAnswer, UnavailableSubjectiveCorrectAnswer}

/**
  * Created by nirmalya on 3/19/18.
  */





// This class represents a rack of questions/answers, based on - in addition to Game's ID and Category - the language in
// which questions and answers had been collected and stored.
case class GameLibraryShelfID(gameID: Int, quizCategory: String, prefLang: String)
case class QnAShelf(
             val contents: List[QuestionAnswerPairPack],
             val qRandomizer:  (List[QuestionAnswerPairPack]) => List[QuestionAnswerPairPack],
             val ansRandomizer:(List[ObjectiveAnswer])         => List[ObjectiveAnswer]) {

  val questionsByScore = contents.groupBy(e => e.ques.score)

  def pickAvailableSubjectiveAnswerWithQuestion(fromScoreBucket: Int): Option[(Question, SubjectiveCorrectAnswer)] = {

    this.questionsByScore.get(fromScoreBucket) match {

      case Some(quesAnsPairPacks) =>


        val pack = qRandomizer(quesAnsPairPacks).head

        // There can be only one Subjective answer, if it is defined
        Some(pack.ques, pack.ansSubjective.getOrElse(UnavailableSubjectiveCorrectAnswer))

      case None => None

    }
  }

  def pickAvailableObjectiveAnswersWithQuestions(fromScoreBucket: Int, countOfOptionsPerAnswer: Int): Option[(Question,List[ObjectiveAnswer])] = {

        this.questionsByScore.get(fromScoreBucket) match {

          case Some(quesAnsPairPacks) =>    // Answers might or might not have been defined for this question/score

            val pack = qRandomizer(quesAnsPairPacks).head

            pack
            .ansObjective match {

              case None             =>   Some(pack.ques,  List(UnavailableObjectiveAnswer))
              case Some(answers)    =>   Some(pack.ques,  ansRandomizer(answers) take (countOfOptionsPerAnswer))

            }

          case None                  => None
        }

  }
}


case class Quiz() { }

object GameLibrary {

  def questRandomizer(original: IndexedSeq[QuestionAnswerPairPack]): IndexedSeq[QuestionAnswerPairPack] = scala.util.Random.shuffle(original)
}

object LibraryUtilities {

  def createPairedQnAPacks(bunchOfQnAFromDBJsonified: List[RawQuestionAnswerScoreTriple]): List[QuestionAnswerPairPack] = {
    transformToPairPacks(bunchOfQnAFromDBJsonified)
  }

  private def prepareReadableQnAPair(quesAnsScoreTripletFromDB: RawQuestionAnswerScoreTriple): QuestionAnswerPairPack = {

    val subjAndObjAnswers =
      AnswerBuilder
        .assembleAvailableAnswers(
          AnswerBuilder
            .unJasonifyAnswers(quesAnsScoreTripletFromDB.ans)
        )

    QuestionAnswerPairPack(
      Question(quesAnsScoreTripletFromDB.ques,quesAnsScoreTripletFromDB.score),
      subjAndObjAnswers._1,  // zero or one Subjective
      subjAndObjAnswers._2   // zero or more Objective
    )
  }

  private def transformToPairPacks(bunchOfQnAFromDBJsonified: List[RawQuestionAnswerScoreTriple]): List[QuestionAnswerPairPack] = {
    bunchOfQnAFromDBJsonified.map(e => prepareReadableQnAPair(e))
  }

}

case class GameLibrary(
             questRandomizer: (List[QuestionAnswerPairPack]) => List[QuestionAnswerPairPack],
             ansRandomizer:   (List[ObjectiveAnswer])        => List[ObjectiveAnswer]) {


  private var  shelves = Map[GameLibraryShelfID,QnAShelf]()
  def reachShelf(shelfID: GameLibraryShelfID): Option[QnAShelf] = {

    if (shelves.isEmpty)   None
    else shelves.get(shelfID)

  }

  def arrangeAShelf(
          shelfID: GameLibraryShelfID,
          bunchOfQnAFromDBJsonified: List[RawQuestionAnswerScoreTriple]): GameLibrary  = {

    val pairedPacks = LibraryUtilities.createPairedQnAPacks(bunchOfQnAFromDBJsonified)

    shelves = shelves + (shelfID -> QnAShelf(pairedPacks,questRandomizer,ansRandomizer))

    this

  }
}


