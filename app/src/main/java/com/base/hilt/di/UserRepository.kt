package com.base.hilt.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.ChallengeListQuery
import com.base.hilt.CreateAccountMutation
import com.base.hilt.LoginMutation
import com.base.hilt.LogoutMutation
import com.base.hilt.VerifySmsOtpMutation
import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.ChallengeListInput
import com.base.hilt.type.LoginInput
import com.base.hilt.type.LoginResponse
import com.base.hilt.type.LogoutResponse
import com.base.hilt.type.OtpInput
import com.base.hilt.type.SignUpInput
import javax.inject.Inject


class UserRepository @Inject constructor (
    val apolloClient: ApolloClient
) : BaseRepository() {

    suspend fun onSignUpApi(input: SignUpInput): ResponseHandler<ApolloResponse<CreateAccountMutation.Data>> {
        return graphQlApiCall {
            apolloClient.mutation(CreateAccountMutation(input)).execute()
        }
    }
    suspend fun onLoginApi(input: LoginInput): ResponseHandler<ApolloResponse<LoginMutation.Data>> {
        return graphQlApiCall {
            apolloClient.mutation(LoginMutation(input)).execute()
        }
    }
    suspend fun verifyOtp(input: OtpInput): ResponseHandler<ApolloResponse<VerifySmsOtpMutation.Data>>{
        return  graphQlApiCall {
            apolloClient.mutation(VerifySmsOtpMutation(input)).execute()
        }
    }
    suspend fun challengeListApi(input: ChallengeListInput): ResponseHandler<ApolloResponse<ChallengeListQuery.Data>> {
        return graphQlApiCall {
            apolloClient.query(ChallengeListQuery(input)).execute()
        }
    }
    suspend fun logoutApi(): ResponseHandler<ApolloResponse<LogoutMutation.Data>>{
        return graphQlApiCall {
            apolloClient.mutation(LogoutMutation()).execute()
        }
    }
}