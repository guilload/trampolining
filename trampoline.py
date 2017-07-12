import functools


class Call(object):

    def __init__(self, func, *args, **kwargs):
        self.func = func
        self.args = args
        self.kwargs = kwargs

    def __call__(self):
        return self.func(*self.args, **self.kwargs)


class Done(object):
    def __init__(self, value):
        self.value = value

    def __call__(self):
        return self.value


def trampoline(func):

    @functools.wraps(func)
    def inner(*args, **kwargs):
        f = Call(func, *args, **kwargs)

        while isinstance(f, Call):
            f = f()
        return f()

    return inner


def trampoline_is_even(n):
    return Done(True) if n == 0 else Call(trampoline_is_odd, n - 1)

def trampoline_is_odd(n):
    return Done(False) if n == 0 else Call(trampoline_is_even, n - 1)


is_even = trampoline(trampoline_is_even)
is_odd = trampoline(trampoline_is_odd)
