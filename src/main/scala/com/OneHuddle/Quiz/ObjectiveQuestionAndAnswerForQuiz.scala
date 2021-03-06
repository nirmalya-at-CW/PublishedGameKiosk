package com.OneHuddle.Quiz

import com.OneHuddle.Quiz.preparation.QnAShelf
import com.OneHuddle.Quiz.preparation.QuizQuestionAnswerProtocol.{AllOfTheAboveObjectiveAnswer, AllowedPerAnswer, NoneOfTheAboveObjectiveAnswer, ObjectiveAnswer, ObjectiveCorrectAnswer, ObjectiveIncorrectAnswer, Question}

/**
  * Created by nirmalya on 3/28/18.
  */
case class ObjectiveQuestionAndAnswerForQuiz(countQues: Int, countOptionsPerAns: Int, shelf: QnAShelf) {

  type QAndObjAnswers = (Question,List[ObjectiveAnswer])

  private val scoreBucketIDs = (1 to countQues) map (n => (n * 100))

  private val emptyOfferedSet = Map[Int, Option[QAndObjAnswers]]()

  private val initializedEmptyOfferedSet:Map[Int,Option[QAndObjAnswers]] =
    scoreBucketIDs.foldLeft(emptyOfferedSet)((accu,bucket) => accu + (bucket -> None))

  private var bucketwiseQnAPairs = Map[Int,Option[QAndObjAnswers]]()

  // First, we try to fill in every score-bucket, with a question/answer pair
  def fillInScoreBuckets: ObjectiveQuestionAndAnswerForQuiz  = {

    this.bucketwiseQnAPairs = scoreBucketIDs.foldLeft(initializedEmptyOfferedSet)((accu,bucket) => {

      val pickedQuesAndObjectiveAnswers = shelf.pickAvailableObjectiveAnswersWithQuestions(fromScoreBucket = bucket,countQues = 1, countOptionsPerAns)
      if (!pickedQuesAndObjectiveAnswers.isEmpty)
        accu   +  (bucket -> Some(pickedQuesAndObjectiveAnswers.get.head)) // Remember, We asked for '1' question/answer pair!
      else
        accu

    })

    this
  }

  // At this point, 'bucketwiseQnAPairs' may have some buckets empty, because possibly, no question was provided
  // with that score, by the database. So we have to make a second run, to fill such empty buckets with questions
  // assigned to random bucket. Conventionally, such a bucket's ID is zero (0). After the partitioning below,left-side Map
  // contains unassigned buckets.
  def fillInLeftOverEmptyScoreBuckets: ObjectiveQuestionAndAnswerForQuiz  = {

    val splitIntoEmptyOrNot = this.bucketwiseQnAPairs.partition(e => e._2.isEmpty)


    if (!splitIntoEmptyOrNot._1.isEmpty) {  // one or more buckets are still empty

      val filledBucketsWithRandomQuestions = shelf.fillWithRandomlyChosenQandAns(splitIntoEmptyOrNot._1,countOptionsPerAns)

      this.bucketwiseQnAPairs = this.bucketwiseQnAPairs ++ filledBucketsWithRandomQuestions
      this
    }
    else
      this
  }

  def ensureCorrectAnswerIsEmbeddedAsNecessary: ObjectiveQuestionAndAnswerForQuiz = {

    this.bucketwiseQnAPairs =
      if (this.countOptionsPerAns == AllowedPerAnswer.FourOptions.id) {
        this.bucketwiseQnAPairs.map(x  => {
          (
            x._1, // Bucket ID
            x._2.map(qNa => (qNa._1,
                             if (allAreCorrect(qNa._2)) // Question and ObjectiveAnswers
                                 addAllOfTheAboveAsNecessary(qNa._2)
                             else
                               if (NoneIsCorrect(qNa._2))
                                   addNoneOfTheAboveAsNecessary(qNa._2)
                               else
                                   qNa._2
                            )
                    )
          )
        })

      }
      else
        this.bucketwiseQnAPairs


    this
  }

  def arrangeInOrderOfScores: IndexedSeq[(Int,Option[(Question,List[ObjectiveAnswer])])] = {

    assert(this.bucketwiseQnAPairs.isEmpty == false)

    this.bucketwiseQnAPairs
      .toList
      .sortBy(e => e._1)
      .toIndexedSeq

  }

  private
  def allAreCorrect(answers: List[ObjectiveAnswer]): Boolean = {

    (answers.collect { case a: ObjectiveIncorrectAnswer => a}.isEmpty)

  }

  private
  def NoneIsCorrect(answers: List[ObjectiveAnswer]): Boolean = {

    (answers.collect { case a: ObjectiveCorrectAnswer => a}.isEmpty)

  }

  private
  def addAllOfTheAboveAsNecessary(answers: List[ObjectiveAnswer]):  List[ObjectiveAnswer] = {

     answers.take(AllowedPerAnswer.FourOptions.id - 1) ++ List(AllOfTheAboveObjectiveAnswer)
  }

  private
  def addNoneOfTheAboveAsNecessary(answers: List[ObjectiveAnswer]): List[ObjectiveAnswer] = {

   answers.take(AllowedPerAnswer.FourOptions.id - 1) ++ List(NoneOfTheAboveObjectiveAnswer)

  }


}
