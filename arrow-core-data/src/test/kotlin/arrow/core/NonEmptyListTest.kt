package arrow.core

import arrow.core.extensions.eq
import arrow.core.extensions.hash
import arrow.core.extensions.nonemptylist.applicative.applicative
import arrow.core.extensions.nonemptylist.bimonad.bimonad
import arrow.core.extensions.nonemptylist.comonad.comonad
import arrow.core.extensions.nonemptylist.eq.eq
import arrow.core.extensions.nonemptylist.eqK.eqK
import arrow.core.extensions.nonemptylist.foldable.foldable
import arrow.core.extensions.nonemptylist.functor.functor
import arrow.core.extensions.nonemptylist.hash.hash
import arrow.core.extensions.nonemptylist.monad.monad
import arrow.core.extensions.nonemptylist.order.order
import arrow.core.extensions.nonemptylist.semialign.semialign
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.nonemptylist.semigroupK.semigroupK
import arrow.core.extensions.nonemptylist.show.show
import arrow.core.extensions.nonemptylist.traverse.traverse
import arrow.core.extensions.nonemptylist.unzip.unzip
import arrow.core.extensions.order
import arrow.core.extensions.show
import arrow.core.test.UnitSpec
import arrow.core.test.generators.genK
import arrow.core.test.generators.nonEmptyList
import arrow.core.test.laws.BimonadLaws
import arrow.core.test.laws.EqKLaws
import arrow.core.test.laws.HashLaws
import arrow.core.test.laws.OrderLaws
import arrow.core.test.laws.SemigroupKLaws
import arrow.core.test.laws.SemigroupLaws
import arrow.core.test.laws.ShowLaws
import arrow.core.test.laws.TraverseLaws
import arrow.core.test.laws.UnzipLaws
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bool
import io.kotest.property.arbitrary.int
import io.kotest.property.forAll
import kotlin.math.max
import kotlin.math.min

class NonEmptyListTest : UnitSpec() {
  init {

    val EQ1 = NonEmptyList.eq(Int.eq())

    testLaws(
      ShowLaws.laws(NonEmptyList.show(Int.show()), EQ1, Arb.nonEmptyList(Arb.int())),
      SemigroupKLaws.laws(
        NonEmptyList.semigroupK(),
        NonEmptyList.genK(),
        NonEmptyList.eqK()),
      BimonadLaws.laws(
        NonEmptyList.bimonad(),
        NonEmptyList.monad(),
        NonEmptyList.comonad(),
        NonEmptyList.functor(),
        NonEmptyList.applicative(),
        NonEmptyList.monad(),
        NonEmptyList.genK(),
        NonEmptyList.eqK()
      ),
      TraverseLaws.laws(NonEmptyList.traverse(), NonEmptyList.applicative(), NonEmptyList.genK(), NonEmptyList.eqK()),
      SemigroupLaws.laws(NonEmptyList.semigroup(), Arb.nonEmptyList(Arb.int()), EQ1),
      HashLaws.laws(NonEmptyList.hash(Int.hash()), Arb.nonEmptyList(Arb.int()), EQ1),
      OrderLaws.laws(NonEmptyList.order(Int.order()), Arb.nonEmptyList(Arb.int())),
      EqKLaws.laws(
        NonEmptyList.eqK(),
        NonEmptyList.genK()
      ),
      UnzipLaws.laws(NonEmptyList.unzip(),
        NonEmptyList.genK(),
        NonEmptyList.eqK(),
        NonEmptyList.foldable()
      )
    )

    "can align lists with different lengths" {
      forAll(Arb.nonEmptyList(Arb.bool()), Arb.nonEmptyList(Arb.bool())) { a, b ->
        NonEmptyList.semialign().run {
          align(a, b).fix().size == max(a.size, b.size)
        }
      }

      forAll(Arb.nonEmptyList(Arb.bool()), Arb.nonEmptyList(Arb.bool())) { a, b ->
        NonEmptyList.semialign().run {
          align(a, b).fix().all.take(min(a.size, b.size)).all {
            it.isBoth
          }
        }
      }
    }
  }
}
