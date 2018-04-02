package com.OneHuddle.DBUTF8

import java.sql.DriverManager
import java.time.LocalDateTime

import org.jooq.SQLDialect
import org.jooq.impl.DSL

import com.OneHuddle.DBUTF8.jOOQ.generated.Tables._

import collection.JavaConverters._


case class Pet(
                val justID: Int,
                val nameUniversal: String,
                val nameEnglish: String,
                val langUsed: String,
                val owner: String,
                val sex: String,
                val birth:  LocalDateTime,
                val timezoneApplicable: String,
                val age: Int
              )


/**
  * Created by nirmalya on 7/3/18.
  */
object UTF8Driver extends App {

  val dbAccessURL = "jdbc:mariadb://localhost:3306/OneHuddle?user=nuovo&password=nuovo123"

  val c = DriverManager.getConnection(dbAccessURL)

  val e = DSL.using(c, SQLDialect.MARIADB)

  val rows = e.select().from(PET).where(PET.JUSTID.eq(5.toShort)).fetchInto(classOf[Pet]).asScala.toList

  assert(
    !rows.isEmpty                     &&
    rows.tail == List.empty && (rows.head.nameEnglish == "Salaam")
  )



}
