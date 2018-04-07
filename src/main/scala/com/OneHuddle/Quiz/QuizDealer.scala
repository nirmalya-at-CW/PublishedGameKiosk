package com.OneHuddle.Quiz


import com.OneHuddle.Quiz.preparation.{GameLibrary, GameLibraryShelfID}
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{DisplayableObjectiveQuestionAndAnswer, DisplayableSubjectiveQuestionAndAnswer, IncorrectShelfForDisplayableObjectiveQuestionAndAnswer, IncorrectShelfNonDisplayableSubjectiveQuestionAndAnswer, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, OnlyTFTypeObjectiveAnswer, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, ReadableAnswerWithCorrectnessIndicated, SubjectiveCorrectAnswer, UnavailableDisplayableObjectiveQuestionAndAnswer, UnavailableDisplayableSubjectiveQuestionAndAnswer}

/**
  * Created by nirmalya on 3/22/18.
  */


class QuizDealer(val library: GameLibrary) {


  def dealASubjectiveQuiz(
          shelfID: GameLibraryShelfID, numQuestionsReqd: Int):
          IndexedSeq[DisplayableSubjectiveQuestionAndAnswer] = {

    this.library.reachShelf(shelfID) match {

      case Some(shelfToPickFrom)  =>
                            OfferedSubjectiveQuestionAndAnswerForQuiz(numQuestionsReqd,shelfToPickFrom)
                             .fillInScoreBuckets
                             .fillInLeftOverEmptyScoreBuckets
                             .arrangeInOrderOfScores
                             .map(pressIntoDisplayableFormSubjective)

      case None         =>  IndexedSeq(IncorrectShelfNonDisplayableSubjectiveQuestionAndAnswer)   // shelf is empty or incorrect
    }

  }

  def dealAnObjectiveQuiz(
            shelfID: GameLibraryShelfID, numQuestionsReqd: Int,optionsPerAnswer: Int):
            IndexedSeq[DisplayableObjectiveQuestionAndAnswer] = {

    this.library.reachShelf(shelfID) match {

      case Some(shelfToPickFrom)  =>
        OfferedObjectiveQuestionAndAnswerForQuiz(numQuestionsReqd, optionsPerAnswer, shelfToPickFrom)
          .fillInScoreBuckets
          .fillInLeftOverEmptyScoreBuckets
          .ensureCorrectAnswerIsEmbeddedAsNecessary
          .arrangeInOrderOfScores
          .map(pressIntoDisplayableFormObjective)

      case None         =>  IndexedSeq(IncorrectShelfForDisplayableObjectiveQuestionAndAnswer)   // shelf is empty or incorrect
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
  def pressIntoDisplayableFormSubjective(offeredForm: (Int, Option[(Question,SubjectiveCorrectAnswer)])): DisplayableSubjectiveQuestionAndAnswer = {

      offeredForm._2 match {

         case Some(quesAndAns)         =>
           DisplayableSubjectiveQuestionAndAnswer(
             quesAndAns._1.score,                       // score
             quesAndAns._1.humanReadableQuestionText,   // question
             quesAndAns._2.humanReadableAnswerText)     // answer

         case None                     =>
           UnavailableDisplayableSubjectiveQuestionAndAnswer

      }
  }

  private
  def pressIntoDisplayableFormObjective(offeredForm: (Int, Option[(Question,List[ObjectiveAnswer])])): DisplayableObjectiveQuestionAndAnswer = {

    offeredForm._2 match {

      case Some(qNa) =>
        DisplayableObjectiveQuestionAndAnswer(
          qNa._1.score,
          qNa._1.humanReadableQuestionText,
          qNa._2.map(indicateCorrectness)
        )
      case None      =>
        UnavailableDisplayableObjectiveQuestionAndAnswer

    }
  }

}
