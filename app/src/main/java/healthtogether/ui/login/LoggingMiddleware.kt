package healthtogether.ui.login

import android.util.Log
import healthtogether.redux.Action
import healthtogether.redux.Middleware
import healthtogether.redux.State
import healthtogether.redux.Store

/**
 * This [Middleware] is responsible for logging every [Action] that is processed to the Logcat, so
 * that we can use this for debugging.
 */
class LoggingMiddleware<S : State, A : Action> : Middleware<S, A> {
    override suspend fun process(action: A, currentState: S, store: Store<S, A>) {
        Log.v(
            "LoggingMiddleware",
            "Processing action: $action; Current state: $currentState"
        )
    }
}