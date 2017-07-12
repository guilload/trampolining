import scala.annotation.tailrec


sealed trait Trampoline[+A]

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
    if (n == 0) Done(true)
    else Call(() => trampolineIsOdd(n - 1))
}

def trampolineIsOdd(n: Int): Trampoline[Boolean] = {
    if (n == 0) Done(false)
    else Call(() => trampolineIsEven(n - 1))
}

def IsEven(n: Int): Boolean = {
    trampoline(trampolineIsEven(n))
}

def IsOdd(n: Int): Boolean = {
    trampoline(trampolineIsOdd(n))
}
