package com.OneHuddle.Quiz


import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, OnlyTFTypeObjectiveAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, SubjectiveCorrectAnswer}

/**
  * Created by nirmalya on 3/22/18.
  */


case class  ReadableAnswerWithCorrectnessIndicated(val humanReadableAnswerText: String, val isCorrect: String)
sealed trait DisplayableQuestionAndAnswer
case class DisplayableSubjectiveQuestionAndAnswer(marks: Int, q: String,   a: String) extends DisplayableQuestionAndAnswer
case class DisplayableObjectiveQuestionAndAnswer (marks: Int, q: String,   a: List[ReadableAnswerWithCorrectnessIndicated]) extends DisplayableQuestionAndAnswer

class QuizButler(val library: GameLibrary) {


  def drawUpASubjectiveQuiz(
          shelfID: GameLibraryShelfID, numQuestionsReqd: Int):
          IndexedSeq[Option[DisplayableSubjectiveQuestionAndAnswer]] = {

    this.library.reachShelf(shelfID) match {

      case Some(shelfToPickFrom)  =>
                            OfferedSubjectiveQuestionAndAnswerForQuiz(numQuestionsReqd,shelfToPickFrom)
                             .fillInScoreBuckets
                             .fillInLeftOverEmptyScoreBuckets
                             .arrangeInOrderOfScores
                             .map(pressIntoDisplayableFormSubjective)

      case None         =>  IndexedSeq(None)   // shelf is empty
    }

  }

  def drawUpAnObjectiveQuiz(
            shelfID: GameLibraryShelfID, numQuestionsReqd: Int,optionsPerAnswer: Int):
            IndexedSeq[Option[DisplayableObjectiveQuestionAndAnswer]] = {

    this.library.reachShelf(shelfID) match {

      case Some(shelfToPickFrom)  =>
        OfferedObjectiveQuestionAndAnswerForQuiz(numQuestionsReqd, optionsPerAnswer, shelfToPickFrom)
          .fillInScoreBuckets
          .fillInLeftOverEmptyScoreBuckets
          .ensureCorrectAnswerIsEmbeddedAsNecessary
          .arrangeInOrderOfScores
          .map(pressIntoDisplayableFormObjective)

      case None         =>  IndexedSeq(None)   // shelf is empty
    }

  }

  private
  def indicateCorrectness(ans: ObjectiveAnswer): ReadableAnswerWithCorrectnessIndicated = {

    ans match {

      case p: ObjectiveCorrectAnswer     => ReadableAnswerWithCorrectnessIndicated(p.humanReadableAnswerText,"Y")
      case q: ObjectiveIncorrectAnswer   => ReadableAnswerWithCorrectnessIndicated(q.humanReadableAnswerText,"N")
    }
  }

  private
  def pressIntoDisplayableFormSubjective(offeredForm: (Int, Option[(Question,SubjectiveCorrectAnswer)])): Option[DisplayableSubjectiveQuestionAndAnswer] = {

      offeredForm._2 match {

         case Some(quesAndAns)         =>   Some(
           DisplayableSubjectiveQuestionAndAnswer(
             quesAndAns._1.score,                       // score
             quesAndAns._1.humanReadableQuestionText,   // question
             quesAndAns._2.humanReadableAnswerText)     // answer
         )

         case None                     =>    Some(
           DisplayableSubjectiveQuestionAndAnswer(
             -1,
             "No questions offered",
             "No answers offered"
           )
         )

      }
  }

  private
  def pressIntoDisplayableFormObjective(offeredForm: (Int, Option[(Question,List[ObjectiveAnswer])])): Option[DisplayableObjectiveQuestionAndAnswer] = {

    offeredForm._2 match {

      case Some(qNa) => Some(
        DisplayableObjectiveQuestionAndAnswer(
          qNa._1.score,
          qNa._1.humanReadableQuestionText,
          qNa._2.map(indicateCorrectness)
        )
      )
      case None      =>  Some(
        DisplayableObjectiveQuestionAndAnswer(
          -1,
          "No questions offered",
          List[ReadableAnswerWithCorrectnessIndicated]()
        )
      )

    }
  }

}
