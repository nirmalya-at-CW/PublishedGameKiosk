package com.OneHuddle.Quiz.preparation

import com.OneHuddle.Quiz.preparation.parsing.AnswerBuilder
import QuizQuestionAnswerProtocol.{ObjectiveAnswer, PossibleAnswer, QAndObjAnswers, Question, QuestionAnswerPairPack, RawQuestionAnswerScoreTriple, SubjectiveCorrectAnswer, UnavailableObjectiveAnswer, UnavailableSubjectiveCorrectAnswer}

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


  def pickAvailableObjectiveAnswersWithQuestions(fromScoreBucket: Int, countQues: Int, countOfOptionsPerAnswer: Int): Option[List[QAndObjAnswers]] = {

    val optionsPlucker = pluckAsManyAnsRequired(countOfOptionsPerAnswer) _

        this.questionsByScore.get(fromScoreBucket) match {

          case None                  =>     // No QnA pack available for this score

            None

          case Some(quesAnsPairPacks) =>    // Randomize Q and A, as needed and then pick up

            val qNaPackOfAllAnsOptions = flattenAnswersAndPairWithQ(qRandomizer(quesAnsPairPacks).take(countQues))

            val qNaWithOnlyAsManyAnsOptions = qNaPackOfAllAnsOptions.map(optionsPlucker)

            Some(qNaWithOnlyAsManyAnsOptions)

        }

  }

  def fillWithRandomlyChosenQandAns(emptyBuckets: Map[Int,Option[QAndObjAnswers]],countOfOptionsPerAnswer: Int ): Map[Int,Option[QAndObjAnswers]] = {

      val optionsPlucker = pluckAsManyAnsRequired(countOfOptionsPerAnswer) _

      // Important: Bucket '0 ' contains all random-scorable questions
      val pickedRandomQandA = flattenAnswersAndPairWithQ(this.qRandomizer(this.questionsByScore(0)))

      val k = (emptyBuckets zip pickedRandomQandA)

      (emptyBuckets zip pickedRandomQandA).foldLeft(Map[Int,Option[QAndObjAnswers]]())((accu,nextZippedEntry) => {

         val quesNansWithAnswerOptionsShortened =  optionsPlucker(nextZippedEntry._2)
         accu + (nextZippedEntry._1._1 -> Some(quesNansWithAnswerOptionsShortened))

      })
  }

  private
  def pluckAsManyAnsRequired(countOfOptionsPerAnswer: Int)(from: QAndObjAnswers): QAndObjAnswers =

    // If we need 'k' options for an answer, we should 'take' only as many
    (from._1, from._2.take(countOfOptionsPerAnswer))

  private
  def flattenAnswersAndPairWithQ(packs: List[QuestionAnswerPairPack]): List[QAndObjAnswers] = {

    packs.map(p => {
      p.ansObjective match {

        case None => (p.ques, List(UnavailableObjectiveAnswer))  // In case, no answer is defined for this Q
        case Some(answers) => (p.ques, ansRandomizer(answers))

      }
    })

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


