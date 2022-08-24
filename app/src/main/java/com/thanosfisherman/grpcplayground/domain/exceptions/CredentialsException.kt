package com.thanosfisherman.grpcplayground.domain.exceptions

import java.lang.RuntimeException

class CredentialsException() :RuntimeException("An error occurred, make sure you are online and logged in")