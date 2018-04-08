package com.OneHuddle.Quiz

import com.OneHuddle.Quiz.preparation.QnAShelf
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AllowedPerAnswer, NoneOfTheAboveObjectiveAnswer, ObjectiveAnswer, ObjectiveCorrectAnswer, Question, SubjectiveCorrectAnswer}

/**
  * Created by nirmalya on 3/23/18.
  */



case class SubjectiveQuestionAndAnswerForQuiz(countQues: Int, shelf: QnAShelf) {

  private val scoreBucketIDs = (1 to countQues) map (n => n * 100)

  private val emptyOfferedSet = Map[Int,Option[(Question,SubjectiveCorrectAnswer)]]()

  private val initializedEmptyOfferedSet:Map[Int,Option[(Question,SubjectiveCorrectAnswer)]] =
    scoreBucketIDs.foldLeft(emptyOfferedSet)((accu,bucket) => accu + (bucket -> None))

  private var bucketwiseQnAPairs = Map[Int,Option[(Question,SubjectiveCorrectAnswer)]]()


  // First, we try to fill in every score-bucket, with a question/answer pair
  def fillInScoreBuckets: SubjectiveQuestionAndAnswerForQuiz  = {

    this.bucketwiseQnAPairs = scoreBucketIDs.foldLeft(initializedEmptyOfferedSet)((accu,bucket) => {

      val pickedQuestionAndSubjectiveAnswer = shelf.pickAvailableSubjectiveAnswerWithQuestion(bucket)
      if (!pickedQuestionAndSubjectiveAnswer.isEmpty)
        accu   +  (bucket -> pickedQuestionAndSubjectiveAnswer)
      else
        accu

    })

    this

  }

  // At this point, 'bucketwiseQnAPairs' may have some buckets empty, because possibly, no question was provided
  // with that score, by the database. So we have to make a second run, to fill such empty buckets with questions
  // assigned to random bucket. Conventionally, such a bucket's ID is zero (0). After the split below,left-side Map
  // contains unassigned buckets.
  def fillInLeftOverEmptyScoreBuckets: SubjectiveQuestionAndAnswerForQuiz  = {


    val splitIntoEmptyOrNot = this.bucketwiseQnAPairs.span(e => e._2.isEmpty)

    if (!splitIntoEmptyOrNot._1.isEmpty) {  // one or more buckets are still empty

      val filledBucketsWithRandomQuestions =
        splitIntoEmptyOrNot
          ._1
          .map(e => e._1)   // We need the bucket identifiers only, viz, 100,200, etc.
          .map(bucket => {

          // '0' is bucketID of random questions, by convention
          val anotherRandomSubjectQandAPair = shelf.pickAvailableSubjectiveAnswerWithQuestion(0)
          if (!anotherRandomSubjectQandAPair.isEmpty)
            (bucket,anotherRandomSubjectQandAPair)
          else
            (bucket,None)

        }).toMap

      this.bucketwiseQnAPairs = this.bucketwiseQnAPairs ++ filledBucketsWithRandomQuestions
      this
    }
    else
      this



  }

  def arrangeInOrderOfScores: IndexedSeq[(Int,Option[(Question,SubjectiveCorrectAnswer)])] = {

    this.bucketwiseQnAPairs
      .toList
      .sortBy(e => e._1)
      .toIndexedSeq

  }

}
