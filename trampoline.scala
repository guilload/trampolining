import scala.annotation.tailrec


sealed trait Trampoline[+A]

object Trampoline {

    def call[A](expr: => Trampoline[A]): Call[A] = Call(() => expr)
    def done[A](value: A): Done[A] = Done(value)

}

case class Call[A](thunk: () => Trampoline[A]) extends Trampoline[A]
case class Done[A](value: A) extends Trampoline[A]


@annotation.tailrec
def trampoline[A](trmpln: Trampoline[A]): A = {
    trmpln match {
        case Call(thunk) => trampoline(thunk())
        case Done(value) => value
    }
}

def trampolineIsEven(n: Int): Trampoline[Boolean] = {
    if (n == 0) Trampoline.done(true)
    else Trampoline.call(trampolineIsOdd(n - 1))
}

def trampolineIsOdd(n: Int): Trampoline[Boolean] = {
    if (n == 0) Trampoline.done(false)
    else Trampoline.call(trampolineIsEven(n - 1))
}

def isEven(n: Int): Boolean = {
    trampoline(trampolineIsEven(n))
}

def isOdd(n: Int): Boolean = {
    trampoline(trampolineIsOdd(n))
}
