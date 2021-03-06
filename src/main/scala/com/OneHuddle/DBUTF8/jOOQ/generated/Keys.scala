/*
 * This file is generated by jOOQ.
*/
package com.OneHuddle.DBUTF8.jOOQ.generated


import com.OneHuddle.DBUTF8.jOOQ.generated.tables.Pet
import com.OneHuddle.DBUTF8.jOOQ.generated.tables.records.PetRecord

import java.lang.Short

import javax.annotation.Generated

import org.jooq.Identity
import org.jooq.UniqueKey
import org.jooq.impl.AbstractKeys

import scala.Array


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>OneHuddle</code> schema.
 */
@Generated(
  value = Array(
    "http://www.jooq.org",
    "jOOQ version:3.10.0"
  ),
  comments = "This class is generated by jOOQ"
)
object Keys {

  // -------------------------------------------------------------------------
  // IDENTITY definitions
  // -------------------------------------------------------------------------

  val IDENTITY_PET = Identities0.IDENTITY_PET

  // -------------------------------------------------------------------------
  // UNIQUE and PRIMARY KEY definitions
  // -------------------------------------------------------------------------

  val KEY_PET_PRIMARY = UniqueKeys0.KEY_PET_PRIMARY

  // -------------------------------------------------------------------------
  // FOREIGN KEY definitions
  // -------------------------------------------------------------------------


  // -------------------------------------------------------------------------
  // [#1459] distribute members to avoid static initialisers > 64kb
  // -------------------------------------------------------------------------

  private object Identities0 extends AbstractKeys {
    val IDENTITY_PET : Identity[PetRecord, Short] = AbstractKeys.createIdentity(Pet.PET, Pet.PET.JUSTID)
  }

  private object UniqueKeys0 extends AbstractKeys {
    val KEY_PET_PRIMARY : UniqueKey[PetRecord] = AbstractKeys.createUniqueKey(Pet.PET, "KEY_pet_PRIMARY", Pet.PET.JUSTID)
  }
}
