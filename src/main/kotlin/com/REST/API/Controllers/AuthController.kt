package com.REST.API.Controllers

import com.REST.API.Security.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    data class AuthRequest(
        val email: String,
        val password: String
    )

    data class RefreshRequest(
        val refreshToken: String
    )

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody refreshRequest: RefreshRequest
    ): AuthService.TokenPair{
        return authService.refreshToken(refreshRequest.refreshToken)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody authRequest: AuthRequest
    ): AuthService.TokenPair {
        return authService.login(authRequest.email, authRequest.email)
    }

    @PostMapping("register")
    fun register(
        @RequestBody authRequest: AuthRequest
    ){
        authService.register(authRequest.email, authRequest.email)
    }

    }
