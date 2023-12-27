package com.vungn.backvietlibrary.util.state

enum class LoginState(message: String) {
    NONE(""),
    LOADING("Loading"),
    SUCCESS("Login success"),
    FAIL("Username or Password is not exactly")
}