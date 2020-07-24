package arrow.core.semigroup

import arrow.higherkind

/**
 * Semigroup which always chooses the last argument
 *
 * ```kotlin:ank
 * import arrow.core.semigroup.Last
 * import arrow.core.extensions.semigroup.last.semigroup.semigroup
 * //sampleStart
 * Last.semigroup<Int>().run { Last(1) + Last(2) }.getLast
 * //sampleEnd
 * ```
 * ```kotlin:ank
 * import arrow.core.None
 * import arrow.core.Option
 * import arrow.core.Some
 * import arrow.core.extensions.option.monoid.monoid
 * import arrow.core.extensions.list.foldable.foldMap
 * //sampleStart
 * listOf(1,2,3,4,5).foldMap(Option.monoid(Last.semigroup<Int>())) { x ->
 *   if (x.rem(2) == 0) None else Some(Last(x))
 * }.map { it.getLast }
 * //sampleEnd
 * ```
 */
@higherkind
data class Last<A>(val getLast: A) : LastOf<A> {
  companion object
}